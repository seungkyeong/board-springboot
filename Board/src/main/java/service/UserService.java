package service;

import java.util.Map;
import java.util.UUID;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import auth.CustomUserDetails;
import constant.AppConstant;
import constant.ExceptionConstant;
import dto.LoginDTO;
import dto.UserDTO;
import entity.Role;
import entity.User;
import exceptionHandle.GeneralException;
import lombok.RequiredArgsConstructor;
import repository.RoleRepository;
import repository.UserRepository;
import util.JwtUtil;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	
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
        
        //Role 조회
        Role role = roleRepository.findByRole(AppConstant.USER)
        	    .orElseThrow(() -> new GeneralException(ExceptionConstant.NOT_FOUND_ROLE.getCode(), ExceptionConstant.NOT_FOUND_ROLE.getMessage()));
        
        //password 암호화
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        
        //회원 system_no 생성
        user.setSysNo(UUID.randomUUID().toString().replace("-", "")); 
        
        //회원 생성
        User userEntity = user.toEntity(role);
        userRepository.save(userEntity);
    }
    
    /* 로그인 */ 
    @Transactional
    public Map<String, String> login(LoginDTO loginDto) throws Exception{
        //Authentication 객체 생성(사용자 로그인 정보)
        Authentication authentication = authenticationManager.authenticate(
        		new UsernamePasswordAuthenticationToken(
        				loginDto.getId(), loginDto.getPassword()
        		)
        );
        
        //사용자 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        //사용자 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser(); 

    	
    	//비밀번호 체크
    	if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
    		throw new GeneralException(ExceptionConstant.PASSWORD_NOT_MATCH.getCode(), ExceptionConstant.PASSWORD_NOT_MATCH.getMessage());
    	}

    	//AccessToken JWT 생성
    	String accessToken = JwtUtil.generateAccessToken(user.getUserId(), user.getSysNo(), user.getRole().getRole());
    	Map<String, String> response = Map.of(AppConstant.ACCESS_TOKEN, accessToken);

        return response;
    }
    
    /* 아이디, 비밀번호 찾기 */ 
    @Transactional
    public String findIdPw(Map<String, String> request) throws Exception{
    	User user;
    	String result;
    	if(request.get(AppConstant.Property.TYPE).equals("findId")) { //아이디 찾기인 경우 
    		user = userRepository.findByEmail(request.get(AppConstant.Property.EMAIL))
        			.orElseThrow(() -> new GeneralException(ExceptionConstant.NOT_FOUND_USER.getCode(), ExceptionConstant.NOT_FOUND_USER.getMessage()));
    		result = user.getUserId();
    	} else { //비밀번호 찾기인 경우 
    		user = userRepository.findByUserIdAndEmail(request.get(AppConstant.Property.ID), request.get(AppConstant.Property.EMAIL))
        			.orElseThrow(() -> new GeneralException(ExceptionConstant.NOT_FOUND_USER.getCode(), ExceptionConstant.NOT_FOUND_USER.getMessage()));
    		result = user.getSysNo();
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
    	//사용자 정보 가져오기 
    	User user = userRepository.findById(request.get(AppConstant.Property.SYSNO))
    			.orElseThrow(() -> new GeneralException(ExceptionConstant.NOT_FOUND_USER.getCode(), ExceptionConstant.NOT_FOUND_USER.getMessage()));

        //현재 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(request.get(AppConstant.Property.CURRENT_PW), user.getPassword())) {
        	throw new GeneralException(ExceptionConstant.PASSWORD_NOT_MATCH.getCode(), ExceptionConstant.PASSWORD_NOT_MATCH.getMessage());
        }

        //새 비밀번호 암호화
        String newPassword = passwordEncoder.encode(request.get(AppConstant.Property.NEW_PW));
        
        //회원 정보 수정
        user.updateUser(newPassword);
    }
    
    /* 비밀번호 재설정 */ 
    @Transactional
    public void resetUserPw(Map<String, String> request) throws Exception{
    	//사용자 정보 가져오기 
    	User user = userRepository.findById(request.get(AppConstant.Property.SYSNO))
    			.orElseThrow(() -> new GeneralException(ExceptionConstant.NOT_FOUND_USER.getCode(), ExceptionConstant.NOT_FOUND_USER.getMessage()));

        //현재 비밀번호 일치 여부 확인
        if (!request.get(AppConstant.Property.NEW_PW).equals(request.get(AppConstant.Property.CONFIRM_PW))) {
        	throw new GeneralException(ExceptionConstant.NEW_CONFIRM_PASSWORD_NOT_MATCH.getCode(), ExceptionConstant.NEW_CONFIRM_PASSWORD_NOT_MATCH.getMessage());
        }

        //새 비밀번호 암호화
        String newPassword = passwordEncoder.encode(request.get(AppConstant.Property.NEW_PW));
        
        //회원 정보 수정
        user.updateUser(newPassword);
    }
}

