package exceptionHandle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import constant.ExceptionConstant;
import dto.ResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(GeneralException.class)
    private ResponseEntity<ResponseDTO<Object>> handleGeneralException(GeneralException e){
		return new ResponseEntity<>(new ResponseDTO<>(e.getCode(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    } 
	
	//GeneralException 나머지 예외 처리
	@ExceptionHandler(Exception.class)
    private ResponseEntity<ResponseDTO<Object>> handleException(Exception e){
		return new ResponseEntity<>(new ResponseDTO<>(ExceptionConstant.INTERNAL_ERROR.getCode(), ExceptionConstant.INTERNAL_ERROR.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    } 
}