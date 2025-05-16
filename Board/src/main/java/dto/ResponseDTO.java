package dto;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

//공통 Response
@Getter @Setter
public class ResponseDTO<T> {
    private Boolean success;	//성공 여부
    private int code;			//에러 코드(성공: 0)
    private String message;		//에러 메시지(성공: "")
    private T data; 			//반환 데이터
        
    
    public ResponseDTO() {
    	this.success = true;
    	this.code = 0;
    	this.message = "";
    	this.data = (T) new ArrayList<>();
    }
    
    public ResponseDTO(T data) {
    	this.success = true;
    	this.code = 0;
    	this.message = "";
    	this.data = data;
    }
    
    public ResponseDTO(int code, String message) {
    	this.success = false;
    	this.code = code;
    	this.message = message;
    	this.data = (T) new ArrayList<>();
    }
}