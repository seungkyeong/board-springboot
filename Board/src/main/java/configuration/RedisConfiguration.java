package configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redisHandle.KeyExpiredListener;
import org.springframework.data.redis.listener.PatternTopic;

/* Redis 설정 */
@Configuration
public class RedisConfiguration {
	/* Redis CRUD용 Template */
	@Bean
    public RedisTemplate<String, Long> redisTemplate(RedisConnectionFactory connectionFactory) {
        System.out.println("Redis Connection Factory: " + connectionFactory.getClass().getName()); //연결 정보 확인
        
        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        return redisTemplate;
    }
	
	/* Redis Message Listener 정의 */
    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory, KeyExpiredListener keyExpiredListener ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        //Key expiration 이벤트 수신
        container.addMessageListener(
                new MessageListenerAdapter(keyExpiredListener), 
                new PatternTopic("__keyevent@0__:expired")
        );

        return container;
    }
}
