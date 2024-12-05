package exceptionHandle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import dto.ErrorResponseDTO;

//에러 처리용 Handler
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(GeneralException.class)
    private ResponseEntity<ErrorResponseDTO> handleExceptionInternal(GeneralException ex){
		ErrorResponseDTO<Object> errorResponse = new ErrorResponseDTO<>(ex.getCode(), ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}