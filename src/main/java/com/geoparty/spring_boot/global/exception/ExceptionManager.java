package com.geoparty.spring_boot.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> appExceptionHandler(BaseException e){
        return ResponseEntity.status(e.getErrorCode().getErrorCode())
                .body(e.getErrorCode().name() + " " + e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(ErrorCode.BAD_REQUEST.getErrorCode())
                .body(ErrorCode.BAD_REQUEST.name() + " " + ex.getParameterName() + ErrorCode.BAD_REQUEST.getErrorMsg());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleException(Throwable ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("INTERNAL_SERVER_ERROR " + ex.getMessage());
    }
}
