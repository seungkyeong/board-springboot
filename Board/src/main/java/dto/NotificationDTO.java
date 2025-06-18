package dto;

import entity.Notification;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class NotificationDTO extends RequestDTO{
    private String boardSysNo;			// 게시물 System No
    private String title;				// 게시물 title
    private Boolean readFlag = false;	//알림 확인 플래그 
    
    public NotificationDTO(CommentDTO comment) {
    	super.setUserId(comment.getBoardCreater());
    	super.setUserSysNo(comment.getBoardCreaterSysNo());
    	boardSysNo = comment.getBoardSysNo();
    	title = comment.getTitle();
    }
    
    public NotificationDTO(Notification notification) {
    	super.setUserId(notification.getUserId());
    	super.setUserSysNo(notification.getUserSysNo());
    	super.setSysNo(notification.getSysNo());
    	super.setCreateDate(notification.getCreateDate());
    	super.setModifyDate(notification.getModifyDate());
    	boardSysNo = notification.getBoardSysNo();
    	title = notification.getTitle();
    	readFlag = notification.getRead();
    }
    
    /* NotificationDTO -> Notification Entity 변환 */
    public Notification toEntity() {
    	Notification notification = Notification.builder()
        		.sysNo(super.getSysNo())
                .userId(super.getUserId())
                .userSysNo(super.getUserSysNo())
                .boardSysNo(boardSysNo)
                .title(title)
                .read(readFlag)
                .build();
        return notification;
    }
    
    /* Notification Entity -> NotificationDTO 변환 */
    public static NotificationDTO fromEntity(Notification noti) {
        return new NotificationDTO(noti);
    }
}


