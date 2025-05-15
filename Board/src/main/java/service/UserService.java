package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import constant.ExceptionConstant;
import dao.BoardDAO;
import dto.UserDTO;
import exceptionHandle.GeneralException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import util.JwtUtil;

@Service
public class UserService {
	@Autowired                     
	private final BoardDAO Boarddao;
	@Autowired
    private RedisTemplate<String, String> redisTemplate;
	

    public UserService(BoardDAO Boarddao) { 
        this.Boarddao = Boarddao;
    }
    
    /* 회원가입 */ 
    public int postUser(UserDTO user) throws Exception{
    	Map<String, String> requestData = new HashMap<String, String>();
    	requestData.put("key", "id");
    	requestData.put("value", user.getId());
   	 	System.out.println("user.getSysno(): " + user.getSysNo());
        if(user.getSysNo() == null) { //신규 생성인 경우 ==
        	//id 중복 체크
        	List<UserDTO> data = Boarddao.checkUser(requestData);
        	if(data.size() > 0) { //중복인 경우 //AlreadyExistsException
        		throw new GeneralException(ExceptionConstant.ALREADY_EXIST_ID.getCode(), ExceptionConstant.ALREADY_EXIST_ID.getMessage());
        	}
        	//email 중복 체크
        	requestData.put("key", "email");
        	requestData.put("value", user.getEmail());
        	data = Boarddao.checkUser(requestData);
        	if(data.size() > 0) { //중복인 경우
        		throw new GeneralException(ExceptionConstant.ALREADY_EXIST_EMAIL.getCode(), ExceptionConstant.ALREADY_EXIST_EMAIL.getMessage());
        	}
        	
        	return Boarddao.createUser(user);
        }else { //수정인 경우 
        	return Boarddao.updateUser(user);
        }
        
    }
    
    /* 로그인 */ 
    public Map<String, String> login(String id, String password, HttpServletResponse response) throws Exception{
    	Map<String, String> requestData = new HashMap<String, String>();
    	requestData.put("key", "id");
    	requestData.put("value", id);
    	Map<String, String> responseData = new HashMap<>();
    	
    	//id 기반 존재 여부 확인
    	List<UserDTO> user = Boarddao.checkUser(requestData);
    	if(user.size() <= 0) {//존재하지 않으면 
    		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), "Non-existent member.");
    	}
    	else{
    		//password 확인
    		if(!user.get(0).getPassword().equals(password)) {
    			throw new GeneralException(ExceptionConstant.OPERATION.getCode(), "Password mismatch.");
    		}
    		
    		//AccessToken jwt 생성
    		String accessToken = JwtUtil.generateAccessToken(id, user.get(0).getSysNo());
    		responseData.put("accessToken", accessToken);
    		//RefreshToken jwt 생성
    		String refreshToken = JwtUtil.generateRefreshToken(id, user.get(0).getSysNo());
//    		responseData.put("refreshToken", refreshToken);
    		
    		//RefreshToken을 Redis에 저장 
    		redisTemplate.opsForValue().set(
    	            "refresh:" + user.get(0).getSysNo(),
    	            refreshToken,
    	            86400000, TimeUnit.MILLISECONDS // 만료 시간 설정
    	    );
    		
    		// Refresh Token을 HttpOnlye 쿠키에 담기
    		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true); // JavaScript에서 접근 불가
//            refreshTokenCookie.setSecure(true); // HTTPS에서만 전송
            refreshTokenCookie.setPath("/"); // 모든 경로에서 접근 가능
            refreshTokenCookie.setMaxAge(24 * 60 * 60); // 24시간 유효
//            refreshTokenCookie.setSameSite("Strict"); // CSRF 방지
            response.addCookie(refreshTokenCookie); // 응답에 쿠키 추가

    		
            response.addCookie(refreshTokenCookie);
        			
    	}
        return responseData;
    }
    
    /* 아이디 찾기/비밀번호 찾기 */ 
    public String findIdPw(Map<String, String> requestData) throws Exception{
    	String find = "";
    	
    	//회원 정보 존재 여부 확인
    	List<UserDTO> user = Boarddao.checkUser(requestData);
    	if(user.size() <= 0) { //존재하지 않으면 
    		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), "Non-existent member.");
    	}
    	else{ //존재하면
    		if(requestData.get("key") == "id") {
    			find = user.get(0).getId();
    		}else {
    			find = user.get(0).getPassword();
    		}
    	}
        return find;
    }
    
    /* 사용자 상세 조회 */ 
    public UserDTO getUserDetail(Map<String, String> requestData) throws Exception{
    	List<UserDTO> user = Boarddao.checkUser(requestData);
    	if(user.size() <= 0) { //존재하지 않으면 
    		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), "Non-existent member.");
    	}
        return user.get(0);
    }
    
    /* 사용자 상세 수정 */ 
    public int updateUserDetail(UserDTO user) throws Exception{
    	//email 중복 확인
    	Map<String, String> requestData = new HashMap<String, String>();
    	
    	requestData.put("key", "email");
    	requestData.put("value", user.getEmail());
    	List<UserDTO> data = Boarddao.checkUser(requestData);
    	if(data.size() > 1) { //중복인 경우
    		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), "Email already exists.");
    	}
    	
    	int result = Boarddao.updateUserDetail(user);
        return result;
    }
    
    /* 비밀번호 수정 */ 
    public int updateUserPw(Map<String, String> requestData) throws Exception{
    	int result = Boarddao.updateUserPw(requestData);
        return result;
    }
}

