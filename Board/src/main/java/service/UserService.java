package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import constant.ExceptionConstant;
import dao.BoardDAO;
import dto.UserDTO;
import exceptionHandle.GeneralException;
import util.JwtUtil;

@Service
public class UserService {
	@Autowired                     
	private final BoardDAO Boarddao;
	

    public UserService(BoardDAO Boarddao) { 
        this.Boarddao = Boarddao;
    }
    
    /* 회원가입 */ 
    public int postUser(UserDTO user) throws Exception{
    	Map<String, String> requestData = new HashMap<String, String>();
    	requestData.put("key", "id");
    	requestData.put("value", user.getId());
   	 	
        if(user.getSysNo() == "") { //신규 생성인 경우 
        	//id 중복 체크
        	List<UserDTO> data = Boarddao.checkUser(requestData);
        	if(data.size() > 0) { //중복인 경우
        		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), "Id already exists.");
        	}
        	//email 중복 체크
        	requestData.put("key", "email");
        	requestData.put("value", user.getEmail());
        	data = Boarddao.checkUser(requestData);
        	if(data.size() > 0) { //중복인 경우
        		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), "Email already exists.");
        	}
        	
        	return Boarddao.createUser(user);
        }else { //수정인 경우 
        	return Boarddao.updateUser(user);
        }
        
    }
    
    /* 로그인 */ 
    public String login(String id, String password) throws Exception{
    	String jwtToken = "";
    	
    	Map<String, String> requestData = new HashMap<String, String>();
    	requestData.put("key", "id");
    	requestData.put("value", id);
    	
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
    		//jwt 생성
    		jwtToken = JwtUtil.generateToken(id, user.get(0).getSysNo());
    	}
        return jwtToken;
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

