package constant;

public enum ExceptionConstant { //enum: 열거형 타입, 주로 상수 관리용으로 사용, 상수명(상태코드, Http 상태값, 메시지), 생성자의 접근제한자가 private임
    //Exception Constant
	OK(0, "Success"),
    BAD_REQUEST(10000, "Bad request"),
    VALIDATION_ERROR(10001, "Validation error"),
    NOT_FOUND(10002, "Requested resource is not found"),
    INTERNAL_ERROR(20000, "Internal error"),
    DATA_ACCESS_ERROR(20001, "Data access error"),
    UNAUTHORIZED(40000, "User unauthorized"),
	OPERATION(20002, "Operation Error");
    
	private int code;
    private String message;
    
    private ExceptionConstant(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
}

