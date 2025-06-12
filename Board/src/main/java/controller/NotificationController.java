package controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import constant.ApiPathConstant;
import dto.NotificationDTO;
import dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import service.NotificationService;

@RequiredArgsConstructor
@RestController 
@RequestMapping(ApiPathConstant.API_ROOT)
public class NotificationController {
    private final NotificationService notificationService; 
    
    /* Noti 알림 조회 */
    @PostMapping(ApiPathConstant.NOTI.GET_LIST)
    public ResponseEntity<ResponseDTO<Object>> getNotiList(@RequestBody NotificationDTO notification) throws Exception {
    	List<NotificationDTO> data = notificationService.getNotiList(notification);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(data));
    }
    
    /* Noti 읽음 Flag 업데이트 */
    @PostMapping(ApiPathConstant.NOTI.UPDATE_LIST)
    public ResponseEntity<ResponseDTO<Object>> updateNotiReadFlag(@RequestBody NotificationDTO notification) throws Exception {
    	notificationService.updateNotiReadFlag(notification);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>());
    }
}


