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
public class Comment {
	@Id
	@Column(name = "system_no")
    private String sysNo; //댓글 System no.

    @Column
    private String comment; //댓글
    
    @Column(name = "id")
    private String userId; //작성자 userId 

    @Column(name = "user_system_no")
    private String userSysNo; //작성자 System no.
    
    @Column(name = "parent_system_no")
    private String parSysNo; //상위 댓글 System no. 
    
    @Column(name = "board_system_no")
    private String boardSysNo; //게시물 System no.  

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
