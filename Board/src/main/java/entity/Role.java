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
public class Role {
	/* Role System no. */
	@Id
	@Column(name = "system_no")
    private String sysNo; 

	/* Role */
    @Column(name = "role")
    private String role; 

    /* 수정일 */
    @Column(name = "modify_date")
    private LocalDateTime modifyDate; 
    
    /* 생성일 */
    @Column(name = "create_date")
    private LocalDateTime createDate; 
    
    public Role(String sysNo) {
        this.sysNo = sysNo;
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
