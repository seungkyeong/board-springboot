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
	@Id
	@Column(name = "system_no")
    private String sysNo; //사용자 System no.

    @Column(name = "id")
    private String userId; // 아이디

    @Column
    private String name; //이름
    
    @Column
    private String password; //비밀번호 

    @Column
    private String email; //이메일
    
    @Column
    private String phone; //핸드폰 번호 

    @Column(name = "modify_date")
    private LocalDateTime modifyDate; //수정일
    
    @Column(name = "create_date")
    private LocalDateTime createDate; //생성일
    
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
