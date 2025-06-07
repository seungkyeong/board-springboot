package redisHandle;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import service.BoardService;

@Component
public class KeyExpiredListener implements MessageListener {
	@Autowired
    private RedisTemplate<String, Long> redisTemplate;
	
	@Autowired
    private BoardService boardService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        System.out.println("Key expired: " + expiredKey);
        String[] arrExpiredKey = expiredKey.split(":");
        Map<String, Object> requestData = new HashMap<String, Object>();

        // "expired:"로 시작하는 키만 처리
        if (expiredKey.startsWith("expired:")) {
        	//타입 찾기
        	String type = arrExpiredKey[1];
        	requestData.put("type", type);
        	
        	//sysNo 찾기 
            String sysNo = arrExpiredKey[2];
            requestData.put("sysNo", sysNo);

            //값 가져오기
            String redisKeyForValue = "count:" + type + ":" + sysNo;
            ValueOperations<String, Long> ops = redisTemplate.opsForValue();
            Long count = ops.get(redisKeyForValue);
            requestData.put("count", count);
            
            // DB에 조회수 반영 로직
            try {
            	System.out.printf("viewCount: %d", count);
            	updateViewCountToDB(requestData);	
            }catch(Exception e) {
            	System.out.println(e.getMessage());
            }
        }
    }

    private void updateViewCountToDB(Map<String, Object> requestData) throws Exception{
        // DB 반영 service 로직 호출
        boardService.syncCount(requestData);
    }
}
