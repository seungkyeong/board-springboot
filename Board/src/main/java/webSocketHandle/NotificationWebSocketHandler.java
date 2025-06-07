package webSocketHandle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    // 유저 ID별 WebSocket 세션을 저장
	@Autowired
    private static final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) { //webSocket 연결 수락시 자동 호출
        String userSysNo = getUserSysNoFromSession(session);
        userSessions.put(userSysNo, session);
        System.out.printf("---------afterConnectionExstablished: %s \n", userSysNo);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        String userSysNo = getUserSysNoFromSession(session);
        userSessions.remove(userSysNo);
    }

    public void sendNotification(String userSysNo, String message) throws IOException {
    	WebSocketSession session = userSessions.get(userSysNo);
               
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
                System.out.println("메시지 전송: " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("세션이 없거나 닫혀있습니다: " + userSysNo);
        }
    }

    private String getUserSysNoFromSession(WebSocketSession session) {
        // WebSocket 연결 시 유저 ID를 세션에서 가져오는 로직 추가 필요
        String query = session.getUri().getQuery(); // 예: "userSysNo=sysNo"
        if (query != null && query.startsWith("userSysNo=")) {
            return query.split("=")[1];
        }
        return null;
    }
}
