package util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "secretUserTokenKey";

    /* AccessToken 생성 */
    public static String generateAccessToken(String userId, String sysNo, String role) {
    	String subject = userId + ":" + sysNo;
    	
    	return JWT.create()
                .withSubject(subject)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 600000)) //5분 후 만료
                .sign(Algorithm.HMAC256(SECRET_KEY)); //서명
    }
    
    /* RefreshToken 생성 */
    public static String generateRefreshToken(String userId, String sysNo) {
    	String subject = userId + ":" + sysNo;
    	
    	return JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000)) //1일 후 만료
                .sign(Algorithm.HMAC256(SECRET_KEY)); //서명
    }

    /* JWT에서 사용자 ID 추출 */
    public static String extractUserId(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        String[] subjects = decodedJWT.getSubject().split(":");
        return subjects[0];
    }
    
    /* JWT에서 사용자 System no 추출 */
    public static String extractUserSysNo(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        String[] subjects = decodedJWT.getSubject().split(":");
        return subjects[1];
    }
    
    /* JWT에서 사용자 Role 추출 */
    public static String extractUserRole(String token) {
    	DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim("role").asString();
    }

    /* JWT 유효성 검사 */
    public static boolean validateToken(String token, String userId) {
        String extractedUserId = extractUserId(token);
        return extractedUserId != null && extractedUserId.equals(userId);
    }
}
