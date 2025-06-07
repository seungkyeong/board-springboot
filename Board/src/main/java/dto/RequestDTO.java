package dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

//공통 Request
@Getter @Setter
public class RequestDTO {
    private String sysNo;
    private String userId;
    private String userSysNo; 
    
    private LocalDateTime createDate;
    
    
    private LocalDateTime modifyDate;
}