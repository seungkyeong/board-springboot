package util;

import com.auth0.jwt.exceptions.JWTVerificationException;

import constant.ApiPathConstant;
import constant.AppConstant;
import constant.ExceptionConstant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    //API Full Path
    private static final String LOGIN_PATH = ApiPathConstant.API_ROOT + ApiPathConstant.USER.LOGIN; 
    private static final String SIGNUP_PATH = ApiPathConstant.API_ROOT + ApiPathConstant.USER.SIGNUP; 
    private static final String FINDIDPW_PATH = ApiPathConstant.API_ROOT + ApiPathConstant.USER.FIND_ID_PW;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

    	//CORS 헤더 추가 (프리플라이트 요청)
        response.setHeader(AppConstant.HttpConfig.HEADER_ALLOW_ORIGIN, AppConstant.HttpConfig.ORIGIN_URL);
        response.setHeader(AppConstant.HttpConfig.HEADER_ALLOW_METHOD, 
        		AppConstant.HttpConfig.HTTP_GET + "," + AppConstant.HttpConfig.HTTP_OPTIONS + "," + AppConstant.HttpConfig.HTTP_POST);
        response.setHeader(AppConstant.HttpConfig.HEADER_ALLOW_HEADER, AppConstant.HttpConfig.HEADER_AUTHORIZATION + "," + AppConstant.HttpConfig.HEADER_CONTENT_TYPE);
        response.setHeader(AppConstant.HttpConfig.HEADER_ALLOW_CREDENTIALS, "true");

        
    	// OPTIONS 요청은 JWT 검증 건너뛰기
        if (AppConstant.HttpConfig.HTTP_OPTIONS.equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        
        // 로그인/회원가입/Id,Pw 찾기 경로는 JWT 검증 건너뛰기
        if (request.getRequestURI().equals(LOGIN_PATH) || request.getRequestURI().equals(SIGNUP_PATH) || request.getRequestURI().equals(FINDIDPW_PATH)) {
            filterChain.doFilter(request, response); //JWT 검사를 건너뛰고, 요청을 다음 필터에 전달
            return;
        }
        
        String authorizationHeader = request.getHeader(AppConstant.HttpConfig.HEADER_AUTHORIZATION); //요청 헤더에서 Authorization 추출
        
        if (authorizationHeader != null && authorizationHeader.startsWith(AppConstant.HttpConfig.HEADER_AUTH_PREFIX)) {
            String token = authorizationHeader.substring(AppConstant.HttpConfig.HEADER_AUTH_PREFIX.length()); // JWT 토큰 추출
            try {
            	//userId Token 추출
            	String userIdFromToken = JwtUtil.extractUserId(token);
            	
                //JWT 검증
                if (JwtUtil.validateToken(token, userIdFromToken)) { //JWT가 유효한 경우 
                	//인증 객체 생성
                    String role = JwtUtil.extractUserRole(token); 
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                            userIdFromToken,
                            null,
                            List.of(new SimpleGrantedAuthority(role))
                        );

                    //Security에 인증 등록
                    SecurityContextHolder.getContext().setAuthentication(authentication); 

                    filterChain.doFilter(request, response); // 다음 필터로 이동
                } else { //JWT가 유효하지 않은 경우, 오류 응답
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ExceptionConstant.UNAUTHORIZED.getMessage());
                }
            } catch (JWTVerificationException e) { //JWT 검증 중 오류 발생한 경우, 오류 응답
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ExceptionConstant.INVALID_EXPIRED_TOKEN.getMessage());
            }
        } else { //Authorization 헤더가 없거나 잘못된 형식일 경우, 오류 응답
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ExceptionConstant.INVALID_AUTH_HEADER.getMessage());
        }
    }
}
