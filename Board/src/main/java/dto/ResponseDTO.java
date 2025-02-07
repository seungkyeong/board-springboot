package dto;

import java.util.ArrayList;
import java.util.Collections;
import constant.ExceptionConstant;
import jakarta.validation.constraints.AssertFalse.List;

//공통 Response
public class ResponseDTO<T> {

    private Boolean success;
    private int code;
    private String message;
    private T data;
    
    public ResponseDTO() {
    	this.data = (T) new ArrayList<>();
    	this.success = true;
    	this.code = ExceptionConstant.OK.getCode();
    	this.message = ExceptionConstant.OK.getMessage();
    	
    }
    
    public ResponseDTO(T data) {
    	this.data = data;
    	this.success = true;
    	this.code = ExceptionConstant.OK.getCode();
    	this.message = ExceptionConstant.OK.getMessage();
    	
    }
    
    // 에러 응답 생성자
//    public ResponseDTO(boolean success, int code, String message) {
//        this.success = success;
//        this.code = code;
//        this.message = message;
//        this.data = (data != null) ? data : getDefaultEmptyValue();
//    }

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}