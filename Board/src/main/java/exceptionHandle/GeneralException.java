package exceptionHandle;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GeneralException extends RuntimeException {
	private final int code;
	private final String message;
    
    public GeneralException(int code, String message) {
        super(message); 
        this.message = message;
        this.code = code;
    }
}