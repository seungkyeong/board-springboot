package dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class LoginDTO extends RequestDTO{
    private String id;		  	//아이디
    private String password;	//비밀번호
}


