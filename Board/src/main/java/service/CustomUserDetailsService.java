package service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import auth.CustomUserDetails;
import constant.ExceptionConstant;
import entity.User;
import lombok.RequiredArgsConstructor;
import repository.UserRepository;


/* Security 인증 과정에서 사용자 정보를 조회하는 서비스 클래스 */
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	/* 사용자 정보 조회 */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findWithRoleByUserId(userId)
            .orElseThrow(() -> new UsernameNotFoundException(ExceptionConstant.NOT_FOUND_USER.getMessage() + userId));
        return new CustomUserDetails(user);
    }
}


