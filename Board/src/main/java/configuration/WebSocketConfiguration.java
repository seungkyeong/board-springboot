package configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import webSocketHandle.NotificationWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

	@Bean  // 📌 Spring이 관리하는 Bean을 주입받음
    public NotificationWebSocketHandler notificationWebSocketHandler() {
        return new NotificationWebSocketHandler();
    }
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new NotificationWebSocketHandler(), "/ws/notifications")
                .setAllowedOrigins("*"); // CORS 허용
    }
}
