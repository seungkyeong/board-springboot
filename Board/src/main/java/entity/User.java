package entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class User {
	/* 사용자 System no. */
	@Id
	@Column(name = "system_no")
    private String sysNo; 

	/* Id */
    @Column(name = "id")
    private String userId; 

    /* 이름 */
    @Column
    private String name; 
    
    /* 비밀번호 */
    @Column
    private String password;  

    /* 이메일 */
    @Column
    private String email; 
    
    /* 핸드폰 번호 */
    @Column
    private String phone;  

    /* 수정일 */
    @Column(name = "modify_date")
    private LocalDateTime modifyDate; 
    
    /* 생성일 */
    @Column(name = "create_date")
    private LocalDateTime createDate; 
    
    /* 사용자 Role System no. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_system_no")
    private Role role;

    /* 회원 정보 수정 */
    public void updateUser(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    
    /* 회원 정보 수정(비밀번호) */
    public void updateUser(String password) {
        this.password = password;
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
