package dto;

//exception은 불변이므로 set이 없는게 나음
public class ErrorResponseDTO<T> {
    private Boolean success;
    private int code;
    private String message;
    
    public ErrorResponseDTO(int code, String message) {
    	this.success = false;
    	this.code = code;
    	this.message = message;
    	
    }

	public Boolean getSuccess() {
		return success;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}