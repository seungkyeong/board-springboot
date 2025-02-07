package exceptionHandle;

import java.util.Collections;

import constant.ExceptionConstant;

public class GeneralException extends RuntimeException {

    private String message;
    private int code;
    
    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

    public GeneralException(String message) {
        super(message); //ExceptionConstant.OPERATION.getMessage()
        this.message = message;
        this.code = ExceptionConstant.INTERNAL_ERROR.getCode();
    }
    
    public GeneralException(int code, String message) {
        super(message); 
        this.message = message;
        this.code = code;
    }
}