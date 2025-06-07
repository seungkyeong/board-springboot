package dto;

import entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class UserDTO extends RequestDTO{
    private String id;		  	//아이디
    private String password;	//비밀번호(암호화 필요)
    private String name;		//이름
    private String email;		//이메일
    private String phone;		//핸드폰 번호
    
    /* User를 UserDTO로 세팅 */
    public UserDTO(User user) {
    	super.setSysNo(user.getSysNo());
    	this.id = user.getUserId();
    	this.password = user.getPassword();
    	this.name = user.getName();
    	this.email = user.getEmail();
    	this.phone = user.getPhone();
    }
    
    /* UserDTO -> User Entity 변환 */
    public User toEntity() {
        User user = User.builder()
        		.sysNo(this.getSysNo())
                .userId(id)
                .name(name)
                .password(password)
                .email(email)
                .phone(phone)
                .build();
        return user;
    }
    
    /* User Entity -> UserDTO 변환 */
    public static UserDTO fromEntity(User user) {
        return new UserDTO(user);
    }
}


