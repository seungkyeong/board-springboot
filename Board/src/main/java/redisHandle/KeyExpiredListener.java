package redisHandle;

import java.util.HashMap;
import java.util.Map;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import constant.AppConstant;
import lombok.RequiredArgsConstructor;
import service.BoardService;

/* Redis Key 만료 리스너 클래스 */
@RequiredArgsConstructor
@Component
public class KeyExpiredListener implements MessageListener {
    private RedisTemplate<String, Long> redisTemplate;
    private BoardService boardService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        String[] arrExpiredKey = expiredKey.split(":");
        Map<String, Object> requestData = new HashMap<String, Object>();

        // 만료된 키만 처리
        if (expiredKey.startsWith(AppConstant.RedisKey.EXPIRED + ":")) {
        	//타입 찾기
        	String type = arrExpiredKey[1];
        	requestData.put(AppConstant.Property.TYPE, type);
        	
        	//sysNo 찾기 
            String sysNo = arrExpiredKey[2];
            requestData.put(AppConstant.Property.SYSNO, sysNo);

            //값 가져오기
            String redisKeyForValue = AppConstant.RedisKey.COUNT + ":" + type + ":" + sysNo;
            ValueOperations<String, Long> ops = redisTemplate.opsForValue();
            Long count = ops.get(redisKeyForValue);
            requestData.put(AppConstant.RedisKey.COUNT, count);
            
            // DB에 조회수 반영 로직
            try {
            	updateViewCountToDB(requestData);	
            }catch(Exception e) {
            	System.out.println(e.getMessage());
            }
        }
    }

    /* 조회수 DB 반영 --> 고쳐야 함 */
    private void updateViewCountToDB(Map<String, Object> requestData) throws Exception{
        // DB 반영 service 로직 호출
        boardService.syncCount(requestData);
    }
}
