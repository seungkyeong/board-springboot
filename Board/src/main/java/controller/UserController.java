package controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import constant.ApiPathConstant;
import constant.AppConstant;
import dto.LoginDTO;
import dto.ResponseDTO;
import dto.UserDTO;
import lombok.RequiredArgsConstructor;
import service.UserService;

@RequiredArgsConstructor
@RestController 
@RequestMapping(ApiPathConstant.API_ROOT)
public class UserController {
    private final UserService userService; 
    
    /* 회원가입 */
    @PostMapping(ApiPathConstant.USER.SIGNUP)
    public ResponseEntity<ResponseDTO<Object>> postUser(@RequestBody UserDTO user) throws Exception {
    	userService.createUser(user);
      	
  		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>());
    }
    
    /* 로그인 */
    @PostMapping(ApiPathConstant.USER.LOGIN)
    public ResponseEntity<ResponseDTO<Object>> login(@RequestBody LoginDTO login) throws Exception {	 
    	Map<String, String> data = userService.login(login); 
    	
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(data));
    }
    
    /* 아이디, 비밀번호 찾기 */
    @PostMapping(ApiPathConstant.USER.FIND_ID_PW)
    public ResponseEntity<ResponseDTO<Object>> findIdPw(@RequestBody Map<String, String> request) throws Exception {
    	String data = userService.findIdPw(request);
    	    
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(data));
    }
    
    /* 회원 상세 정보 조회 */
    @PostMapping(ApiPathConstant.USER.GET_DETAIL)
    public ResponseEntity<ResponseDTO<Object>> getUserDetail(@RequestBody Map<String, String> userSysNo) throws Exception {
    	UserDTO data = userService.getUserDetail(userSysNo.get(AppConstant.Property.USER_SYSNO));
    	
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(data));
    }
    
    /* 회원 정보 수정 */
    @PostMapping(ApiPathConstant.USER.UPDATE_DETAIL)
    public ResponseEntity<ResponseDTO<Object>> updateUserDetail(@RequestBody UserDTO user) throws Exception {
    	userService.updateUserDetail(user);
        
  		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>());
    }
    
    /* 회원 정보 수정(비밀번호) */
    @PostMapping(ApiPathConstant.USER.UPDATE_PW)
    public ResponseEntity<ResponseDTO<Object>> updateUserPw(@RequestBody Map<String, String> request) throws Exception {
    	userService.updateUserPw(request);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>());
    }
    
    /* 비밀번호 재설정 */
    @PostMapping(ApiPathConstant.USER.RESET_PW)
    public ResponseEntity<ResponseDTO<Object>> resetUserPw(@RequestBody Map<String, String> request) throws Exception {
    	userService.resetUserPw(request);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>());
    }
}


