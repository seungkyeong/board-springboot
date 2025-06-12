package service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import constant.ExceptionConstant;
import dto.CommentDTO;
import dto.NotificationDTO;
import entity.Notification;
import exceptionHandle.GeneralException;
import lombok.RequiredArgsConstructor;
import repository.NotificationRepository;

@RequiredArgsConstructor
@Service
public class NotificationService {
	private final NotificationRepository notificationRepository;
	
    /* Notification 저장 */ 
	@Transactional
    public void saveNotification (CommentDTO comment) throws Exception {
    	NotificationDTO notification = new NotificationDTO(comment);
    	notification.setSysNo(UUID.randomUUID().toString().replace("-", "")); //알림 system_no 생성
    	
    	//Notification 생성
    	notificationRepository.save(notification.toEntity());
    }
    
    /* 알림 조회 */ 
	@Transactional
    public List<NotificationDTO> getNotiList(NotificationDTO notificationDto) throws Exception{
    	List<NotificationDTO> notiList = new ArrayList<>();
    	
    	//알림 조회
    	List<Notification> notifications = notificationRepository.findByUserSysNoOrderByCreateDateDesc(notificationDto.getUserSysNo());
    	
    	//Noti DTO로 변환
    	notifications.forEach(notification -> {
    	    NotificationDTO notiDto = NotificationDTO.fromEntity(notification);
    	    notiList.add(notiDto);
    	});
    	    	
    	return notiList; 
    }
    
    /* Noti 읽음 Flag 업데이트 */ 
    @Transactional
    public void updateNotiReadFlag(NotificationDTO notification) throws Exception{
    	//Noti 조회
    	Notification noti = notificationRepository.findById(notification.getSysNo())
    			.orElseThrow(() -> new GeneralException(ExceptionConstant.NOT_FOUND_NOTI.getCode(), ExceptionConstant.NOT_FOUND_NOTI.getMessage()));

    	//Noti read 수정
    	noti.updateNotiRead();
    }
}

