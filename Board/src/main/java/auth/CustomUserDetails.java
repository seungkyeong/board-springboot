package auth;

import java.util.Collections;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/* Spring Security로 인증한 사용자 정보를 담는 클래스 */
@RequiredArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {
    private final User user; 

    /* 사용자 권한 반환 */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	return Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().getRole()) 
            );
    }
	
    @Override
    public String getUsername() {
        return user.getUserId(); 
    }

    @Override
    public String getPassword() {
        return user.getPassword(); 
    }
}

