package constant;

import lombok.Getter;

@Getter
public enum ExceptionConstant { 
	INTERNAL_ERROR(20000, "Internal server error."),
	ALREADY_EXIST_ID(20001, "ID is already exists."),
	ALREADY_EXIST_EMAIL(20002, "Email is already exists."),
	NOT_FOUND_USER(20003, "User not found."),
	PASSWORD_NOT_MATCH(20004, "Password does not match."),
	NOT_FOUND_BOARD(20005, "Board not found."),
	
	
	//얘네 한번 확인
	OK(0, "Success"),
    BAD_REQUEST(10000, "Bad request"),
    VALIDATION_ERROR(10001, "Validation error"),
    UNAUTHORIZED(40000, "User unauthorized"),
	OPERATION(20002, "Operation Error");

    
	private final int code;
    private final String message;
    
    private ExceptionConstant(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

