package webSocketHandle;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/* WebSocket 클래스 */
@RequiredArgsConstructor
@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler {
    // 유저 ID별 WebSocket 세션을 저장
    private static final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    /* webSocket 연결 성공시 자동 호출 */
    /* 해당 사용자와의 세션을 userSessions 맵에 저장 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) { 
        String userSysNo = getUserSysNoFromSession(session);
        userSessions.put(userSysNo, session);
    }

    /* webSocket 연결 종료시 자동 호출 */
    /* 해당 사용자와의 세션을 userSessions 맵에서 삭제 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        String userSysNo = getUserSysNoFromSession(session);
        userSessions.remove(userSysNo);
    }

    /* 사용자에게 알림 메시지 전송 */
    public void sendNotification(String userSysNo, String message) throws IOException {
    	//세션 가져오기 
    	WebSocketSession session = userSessions.get(userSysNo);
               
        if (session != null && session.isOpen()) {
            try {
            	//메시지 전송
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("세션이 없거나 닫혀있습니다: " + userSysNo);
        }
    }

    /* WebSocket 요청의 URI에서 사용자 SysNo 추출 */
    private String getUserSysNoFromSession(WebSocketSession session) {
        // WebSocket 연결 시 유저 ID를 세션에서 가져오는 로직 추가 필요
        String query = session.getUri().getQuery(); 
        if (query != null && query.startsWith("userSysNo=")) {
            return query.split("=")[1];
        }
        return null;
    }
}
