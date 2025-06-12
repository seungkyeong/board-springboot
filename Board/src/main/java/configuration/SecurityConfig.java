package configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import constant.ApiPathConstant;
import lombok.RequiredArgsConstructor;
import util.JwtRequestFilter;

/* Spring Security 설정 */
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
	private final JwtRequestFilter jwtRequestFilter;
	
	/* API Full Path */ 
	private static final String SIGNUP_PATH = ApiPathConstant.API_ROOT + ApiPathConstant.USER.SIGNUP;
	private static final String LOGIN_PATH = ApiPathConstant.API_ROOT + ApiPathConstant.USER.LOGIN;
	private static final String FIND_ID_PW_PATH = ApiPathConstant.API_ROOT + ApiPathConstant.USER.FIND_ID_PW;
	
	/* password 암호화 */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }
    
    /* AuthenticationProvider에 인증 위임 */
    /* (CustomUserDetailsService는 Provider를 사용) */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    /* Spring Security 필터 체인 설정 */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    	JsonUsernamePasswordAuthenticationFilter jsonFilter = new JsonUsernamePasswordAuthenticationFilter();
        jsonFilter.setAuthenticationManager(authenticationManager);

    	http
         .csrf(csrf -> csrf.disable()) 													//CSRF 비활성화
    	.addFilterAt(jsonFilter, UsernamePasswordAuthenticationFilter.class)			//커스텀 필터(jsonFilter) 등록
        .authorizeHttpRequests(auth -> auth
        	.requestMatchers(SIGNUP_PATH, LOGIN_PATH, FIND_ID_PW_PATH).permitAll() 					//허용 경로 
        	.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 					//preflight 허용 
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); //Security 인증 전, JWT 인증 수행

        return http.build();
    }

}

