package com.sampleDataBase.shared;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleDeserilizationErrors(HttpMessageNotReadableException exception){
        Map<String,Object> responseBody = new HashMap<>();
        responseBody.put("status", HttpStatus.BAD_REQUEST);
        responseBody.put("error", exception.getMostSpecificCause().getMessage());
        return new ResponseEntity<>(responseBody,HttpStatus.BAD_REQUEST);
    }
}
