package service;

import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import constant.ExceptionConstant;
import dto.UserDTO;
import entity.User;
import exceptionHandle.GeneralException;
import lombok.RequiredArgsConstructor;
import repository.UserRepository;
import util.JwtUtil;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	
    /* 회원가입 */ 
    @Transactional
    public void createUser(UserDTO user) throws Exception{
        //id 중복 체크
        boolean isDuplicated = userRepository.existsByUserId(user.getId());
        if(isDuplicated) {
        	throw new GeneralException(ExceptionConstant.ALREADY_EXIST_ID.getCode(), ExceptionConstant.ALREADY_EXIST_ID.getMessage());
        }
        	
        //email 중복 체크
        isDuplicated = userRepository.existsByEmail(user.getEmail());
        if(isDuplicated) { //중복인 경우
        	throw new GeneralException(ExceptionConstant.ALREADY_EXIST_EMAIL.getCode(), ExceptionConstant.ALREADY_EXIST_EMAIL.getMessage());
        }
        
        //회원 생성
        user.setSysNo(UUID.randomUUID().toString().replace("-", "")); //회원 system_no 생성
        userRepository.save(user.toEntity());
    }
    
    /* 로그인 */ 
    @Transactional
    public Map<String, String> login(Map<String, String> request) throws Exception{
    	String id = request.get("id");
    	String password = request.get("password");
    	
    	//회원 정보 조회
    	User user = userRepository.findByUserId(id)
    			.orElseThrow(() -> new GeneralException(ExceptionConstant.NOT_FOUND_USER.getCode(), ExceptionConstant.NOT_FOUND_USER.getMessage()));

    	//비밀번호 체크
    	if(!user.getPassword().equals(password)) {
    		throw new GeneralException(ExceptionConstant.PASSWORD_NOT_MATCH.getCode(), ExceptionConstant.PASSWORD_NOT_MATCH.getMessage());
    	}
    		
    	//AccessToken JWT 생성
    	String accessToken = JwtUtil.generateAccessToken(id, user.getSysNo());
    	Map<String, String> response = Map.of("accessToken", accessToken);

        return response;
    }
    
    /* 아이디, 비밀번호 찾기 */ 
    @Transactional
    public String findIdPw(Map<String, String> request) throws Exception{
    	User user;
    	String result;
    	if(request.get("type").equals("findId")) { //아이디 찾기인 경우 
    		user = userRepository.findByEmail(request.get("email"))
        			.orElseThrow(() -> new GeneralException(ExceptionConstant.NOT_FOUND_USER.getCode(), ExceptionConstant.NOT_FOUND_USER.getMessage()));
    		result = user.getUserId();
    	} else { //비밀번호 찾기인 경우 
    		user = userRepository.findByUserId(request.get("id"))
        			.orElseThrow(() -> new GeneralException(ExceptionConstant.NOT_FOUND_USER.getCode(), ExceptionConstant.NOT_FOUND_USER.getMessage()));
    		result = user.getPassword();
    	}
    	
        return result;
    }
    
    /* 회원 상세 정보 조회 */
    public UserDTO getUserDetail(String userSysNo) throws Exception{
    	User user = userRepository.findById(userSysNo)
    			.orElseThrow(() -> new GeneralException(ExceptionConstant.NOT_FOUND_USER.getCode(), ExceptionConstant.NOT_FOUND_USER.getMessage()));

    	UserDTO userDto = UserDTO.fromEntity(user);

        return userDto;
    }
    
    /* 회원 정보 수정 */ 
    @Transactional
    public void updateUserDetail(UserDTO userDto) throws Exception{
    	//email 중복 체크
        boolean isDuplicated = userRepository.existsByEmailAndSysNoNot(userDto.getEmail(), userDto.getSysNo());
        if(isDuplicated) { //중복인 경우
        	throw new GeneralException(ExceptionConstant.ALREADY_EXIST_EMAIL.getCode(), ExceptionConstant.ALREADY_EXIST_EMAIL.getMessage());
        }
    	
        //회원 정보 조회
        User user = userRepository.findById(userDto.getSysNo())
    			.orElseThrow(() -> new GeneralException(ExceptionConstant.NOT_FOUND_USER.getCode(), ExceptionConstant.NOT_FOUND_USER.getMessage()));

        //회원 정보 수정
        user.updateUser(userDto.getName(), userDto.getEmail(), userDto.getPhone());
    }
    
    /* 회원 정보 수정(비밀번호) */ 
    @Transactional
    public void updateUserPw(Map<String, String> request) throws Exception{
    	//회원 정보 조회
        User user = userRepository.findById(request.get("sysNo"))
    			.orElseThrow(() -> new GeneralException(ExceptionConstant.NOT_FOUND_USER.getCode(), ExceptionConstant.NOT_FOUND_USER.getMessage()));

        //회원 정보 수정
        user.updateUser(request.get("newPassword"));
    }
}

