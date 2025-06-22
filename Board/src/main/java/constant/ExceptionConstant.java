package constant;

import lombok.Getter;

/* Exception 관련 Constant */
@Getter
public enum ExceptionConstant { 
	OK(0, "Success"),
	INTERNAL_ERROR(20000, "Internal server error."),
	ALREADY_EXIST_ID(20001, "ID is already exists."),
	ALREADY_EXIST_EMAIL(20002, "Email is already exists."),
	NOT_FOUND_USER(20003, "User not found."),
	PASSWORD_NOT_MATCH(20004, "Password does not match."),
	NOT_FOUND_BOARD(20005, "Board not found."),
	NOT_FOUND_NOTI(20006, "Notification not found."),
	NOT_FOUND_ROLE(20007, "Role not found."),
	UNAUTHORIZED(20008, "Unauthorized."),
	INVALID_EXPIRED_TOKEN(20009, "Invalid or expired token."),
	INVALID_AUTH_HEADER(20010, "Authorization header is missing or invalid"),
	NEW_CONFIRM_PASSWORD_NOT_MATCH(20011, "The new password and confirmation password do not match."),
	NOT_FOUND_COMMENT(20012, "Comment not found."),
	
	//얘네 한번 확인
    BAD_REQUEST(10000, "Bad request"),
    VALIDATION_ERROR(10001, "Validation error"),
	OPERATION(20002, "Operation Error");

    
	private final int code;
    private final String message;
    
    private ExceptionConstant(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

