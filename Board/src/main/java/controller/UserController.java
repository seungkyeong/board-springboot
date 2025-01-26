package controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import constant.ExceptionConstant;
import dto.ResponseDTO;
import dto.UserDTO;
import exceptionHandle.GeneralException;
import service.UserService;

@RestController //RESTful API를 작성하기 위한 어노테이션 
@RequestMapping("/api/board")
public class UserController {
	@Autowired
    private final UserService userService; //필드 선언, private: 해당 클래스에서만 접근 가능, final: 초기화 후 변경 불가능
	
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    // 회원가입
    @PostMapping("/signUp")
    public ResponseDTO<Object> postUser(@RequestBody UserDTO user) throws Exception {
    	int data = userService.postUser(user);
      
      	if(data == 0) { //게시물 생성 & 수정 실패했을 경우
      		System.out.println("Operation Error");
      		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), ExceptionConstant.OPERATION.getMessage());
      	}else { //게시물 생성 & 수정 성공했을 경우 
  		
      	}
  		return new ResponseDTO<>(data);
    }
    
    // 로그인
    @PostMapping("/login")
    public ResponseDTO<Object> login(@RequestBody Map<String, String> loginData) throws Exception {	 
    	String id = loginData.get("id");
    	String password = loginData.get("password");
    	 
    	String data = userService.login(id, password); //jwtToken
    	    
    	return new ResponseDTO<>(data);
    }
    
    //아이디 찾기, 비밀번호 찾기
    @PostMapping("/findIdPw")
    public ResponseDTO<Object> findIdPw(@RequestBody Map<String, String> findData) throws Exception {
    	String findKey = findData.keySet().iterator().next();
   	 	String findValue = findData.get(findKey);
   	 
    	Map<String, String> requestData = new HashMap<String, String>();
    	requestData.put("key", findKey);
    	requestData.put("value", findValue);
    	 
    	String data = userService.findIdPw(requestData);
    	    
    	return new ResponseDTO<>(data);
    }
    
    // 회원 정보 조회
    @PostMapping("/userDetail")
    public ResponseDTO<Object> getUserDetail(@RequestBody Map<String, String> findData) throws Exception {
    	String findKey = findData.keySet().iterator().next();
   	 	String findValue = findData.get(findKey);
   	 
    	Map<String, String> requestData = new HashMap<String, String>();
    	requestData.put("key", findKey);
    	requestData.put("value", findValue);
    	
    	UserDTO data = userService.getUserDetail(requestData);
    	
    	return new ResponseDTO<>(data);
    }
    
    // 회원 정보 수정
    @PostMapping("/updateUserDetail")
    public ResponseDTO<Object> updateUserDetail(@RequestBody UserDTO user) throws Exception {
    	int data = userService.updateUserDetail(user);
        
      	if(data == 0) { //게시물 생성 & 수정 실패했을 경우
      		System.out.println("Operation Error");
      		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), ExceptionConstant.OPERATION.getMessage());
      	}
  		return new ResponseDTO<>(data);
    }
    
    // 비밀번호 변경
    @PostMapping("/updateUserPw")
    public ResponseDTO<Object> updateUserPw(@RequestBody Map<String, String> requestData) throws Exception {
    	int data = userService.updateUserPw(requestData);
    	if(data == 0) { //비밀번호 수정 실패했을 경우
      		System.out.println("Operation Error");
      		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), ExceptionConstant.OPERATION.getMessage());
      	}
    	
    	return new ResponseDTO<>(data);
    }
}


