package util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "secretUserTokenKey";

    // JWT 생성
    public static String generateToken(String userId, String sysNo) {
    	String subject = userId + ":" + sysNo;
    	
    	return JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000)) // 1시간 후 만료
                .sign(Algorithm.HMAC256(SECRET_KEY)); // 서명
    }

    // JWT에서 사용자 ID 추출
    public static String extractUserId(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getSubject();
    }

    // JWT 유효성 검사
    public static boolean validateToken(String token, String userId) {
        String extractedUserId = extractUserId(token);
        return extractedUserId != null && extractedUserId.equals(userId);
    }
}
