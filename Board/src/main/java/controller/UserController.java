package controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dto.ResponseDTO;
import dto.UserDTO;
import lombok.RequiredArgsConstructor;
import service.UserService;

@RequiredArgsConstructor
@RestController 
@RequestMapping("/api/board")
public class UserController {
    private final UserService userService; 
    
    /* 회원가입 */
    @PostMapping("/signUp")
    public ResponseEntity<ResponseDTO<Object>> postUser(@RequestBody UserDTO user) throws Exception {
    	userService.createUser(user);
      	
  		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>());
    }
    
    /* 로그인 */
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<Object>> login(@RequestBody Map<String, String> request) throws Exception {	 
    	Map<String, String> data = userService.login(request); 
    	
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(data));
    }
    
    /* 아이디, 비밀번호 찾기 */
    @PostMapping("/findIdPw")
    public ResponseEntity<ResponseDTO<Object>> findIdPw(@RequestBody Map<String, String> request) throws Exception {
    	String data = userService.findIdPw(request);
    	    
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(data));
    }
    
    /* 회원 상세 정보 조회 */
    @PostMapping("/userDetail")
    public ResponseEntity<ResponseDTO<Object>> getUserDetail(@RequestBody Map<String, String> userSysNo) throws Exception {
    	UserDTO data = userService.getUserDetail(userSysNo.get("userSysNo"));
    	
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(data));
    }
    
    /* 회원 정보 수정 */
    @PostMapping("/updateUserDetail")
    public ResponseEntity<ResponseDTO<Object>> updateUserDetail(@RequestBody UserDTO user) throws Exception {
    	userService.updateUserDetail(user);
        
  		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>());
    }
    
    /* 회원 정보 수정(비밀번호) */
    @PostMapping("/updateUserPw")
    public ResponseEntity<ResponseDTO<Object>> updateUserPw(@RequestBody Map<String, String> request) throws Exception {
    	userService.updateUserPw(request);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>());
    }
}


