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
public class Board {
	/* 게시물 System no. */
	@Id
	@Column(name = "system_no")
    private String sysNo; 			

	/* 제목 */
    @Column
    private String title; 			

    /* 내용 */
    @Column
    private String content; 
    
    /* 작성자 Id */
    @Column(name = "id")
    private String userId; 

    /* 작성자 System no. */
    @Column(name = "user_system_no")
    private String userSysNo;
    
    /* 조회수 */
    @Column
    private long view; 
    
    /* 좋아요 수 */
    @Column(name = "like_count")
    private long like;  
    
    /* 이미지 저장 경로 */
    @Column(name = "img_path")
    private String imgPath; 

    /* 수정일 */
    @Column(name = "modify_date")
    private LocalDateTime modifyDate; 
    
    /* 생성일 */
    @Column(name = "create_date")
    private LocalDateTime createDate; 
    
    /* 게시물 수정 */
    public void updateBoard(String title, String content, String imgPath) {
        this.title = title;
        this.content = content;
        this.imgPath = imgPath;
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
