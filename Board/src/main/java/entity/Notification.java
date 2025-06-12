package entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Notification {
	/* 알림 System no. */
	@Id
	@Column(name = "system_no")
    private String sysNo; 

	/* 알림 대상자 Id */
    @Column(name = "id")
    private String userId; 

    /* 알림 대상자 System no. */
    @Column(name = "user_system_no")
    private String userSysNo; 
    
    /* 게시물 System no. */
    @Column(name = "board_system_no")
    private String boardSysNo; 

    /* 게시물 제목 */
    @Column
    private String title; 
    
    /* 알림 읽음 여부 */
    @Column(name = "read_flag")
    private Boolean read; 

    /* 수정일 */
    @Column(name = "modify_date")
    private LocalDateTime modifyDate; 
    
    /* 생성일 */
    @Column(name = "create_date")
    private LocalDateTime createDate; 
    
    /* Noti 수정 */
    public void updateNotiRead() {
        this.read = true;
    }
    
    /* Insert 이전에 실행 */
    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();	
        this.modifyDate = this.createDate;
    }

    /* Update 이전에 실행*/
    @PreUpdate
    public void preUpdate(){
        this.modifyDate = LocalDateTime.now();
    }
}
