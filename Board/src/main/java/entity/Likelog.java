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
public class Likelog {
	@Id
	@Column(name = "system_no")
    private String sysNo; //좋아요 로그 System no.
    
    @Column(name = "id")
    private String userId; //좋아요 누른 userId 

    @Column(name = "user_system_no")
    private String userSysNo; //좋아요 누른 System no.
    
    @Column(name = "board_system_no")
    private String boardSysNo; //좋아요 누른 게시글 System no. 

    @Column(name = "modify_date")
    private LocalDateTime modifyDate; //수정일
    
    @Column(name = "create_date")
    private LocalDateTime createDate; //생성일
    
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
