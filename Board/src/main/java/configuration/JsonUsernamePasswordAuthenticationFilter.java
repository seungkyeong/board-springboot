package configuration;

import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import dto.LoginDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/* JSON 형식의 로그인을 파싱하여 Security가 인증할 수 있도록 처리하는 필터 */
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private static final String INVALID_LOGIN_REQ = "Invalid Login Request.";
	
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) { //JSON가 아닌 경우, 기존 방식으로 처리
            return super.attemptAuthentication(request, response);
        }
        
        //JSON 처리
        try (InputStream is = request.getInputStream()) {
        	//JSON을 LoginDTO로 변환
            ObjectMapper mapper = new ObjectMapper();
            LoginDTO loginDto = mapper.readValue(is, LoginDTO.class); 
            
            //인증 토큰 생성
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginDto.getId(), loginDto.getPassword());
            setDetails(request, authRequest);
            
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            throw new AuthenticationServiceException(INVALID_LOGIN_REQ);
        }
    }
}

