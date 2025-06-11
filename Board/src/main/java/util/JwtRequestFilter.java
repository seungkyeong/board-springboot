package util;

import com.auth0.jwt.exceptions.JWTVerificationException;
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
	
    private static final String HEADER = "Authorization"; // Authorization 헤더에 JWT 토큰이 포함됨
    private static final String PREFIX = "Bearer "; // JWT 토큰 앞에 "Bearer "가 붙어 있음
    private static final String LOGIN_PATH = "/api/board/login"; // 로그인 API 경로
    private static final String SIGNUP_PATH = "/api/board/signUp";
    private static final String FINDIDPW_PATH = "/api/board/findIdPw";
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

    	// CORS 헤더 추가 (프리플라이트 요청)
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        
    	// OPTIONS 요청은 JWT 검증을 건너뛰기
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        
        // 로그인 경로는 JWT 검증을 건너뛰기
        if (request.getRequestURI().equals(LOGIN_PATH) || request.getRequestURI().equals(SIGNUP_PATH) || request.getRequestURI().equals(FINDIDPW_PATH)) {
            filterChain.doFilter(request, response); // JWT 검사를 건너뛰고 요청을 다음 필터나 서블릿으로 전달
            return;
        }
        
        String authorizationHeader = request.getHeader(HEADER); // 요청 헤더에서 토큰 추출
        System.out.println("authorizationHeader");
        System.out.println(authorizationHeader);
        
        if (authorizationHeader != null && authorizationHeader.startsWith(PREFIX)) {
            String token = authorizationHeader.substring(PREFIX.length()); // "Bearer " 이후의 토큰 부분 추출
            System.out.println("token");
            System.out.println(token);
            try {
            	//userId Token 추출
            	String userIdFromToken = JwtUtil.extractUserId(token);
            	System.out.println("userIdFromToken");
                System.out.println(userIdFromToken);
            	
                //JWT 검증
                if (JwtUtil.validateToken(token, userIdFromToken)) { //JWR가 유효한 경우 
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
                } else { // JWT가 유효하지 않은 경우, 인증 오류 응답
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                }
            } catch (JWTVerificationException e) { // JWT 검증 중 오류 발생 시
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
            }
        } else { // Authorization 헤더가 없거나 잘못된 형식일 경우
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing or invalid");
        }
    }
}
