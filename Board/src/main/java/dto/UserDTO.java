package dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDTO extends RequestDTO{
    private String id;		  	//아이디
    private String password;	//비밀번호(암호화 필요)
    private String name;		//이름
    private String email;		//이메일
    private String phone;		//핸드폰 번호
}


