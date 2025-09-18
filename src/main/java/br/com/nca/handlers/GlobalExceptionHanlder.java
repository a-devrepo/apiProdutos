package br.com.nca.handlers;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import br.com.nca.domain.dtos.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHanlder {

	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            WebRequest request) {
        
        var errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> "Campo: '" + error.getField() + "' : " + error.getDefaultMessage())
                .collect(Collectors.toList());
        
        var body = ApiErrorResponse.builder().timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .errors(errors)
                .build();
                
        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}