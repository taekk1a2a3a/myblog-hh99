package com.sparta.myblog.exception;

import com.sparta.myblog.dto.ResponseMsgDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity handleCustomException(final CustomException e) {
        log.error("handleCustomException: {}", e.getStatusEnum());
        LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("status", e.getStatusEnum().getStatus());
        responseMap.put("message", e.getStatusEnum().getMessage());
        return ResponseEntity
                .status(e.getStatusEnum().getStatus())
                .body(responseMap);
    }

    // Valid 예외 핸들러
    @ExceptionHandler(value = {BindException.class})
    public ResponseMsgDto handleBindException(BindException  ex) {
        BindingResult bindingResult = ex.getBindingResult();

        StringBuilder sb = new StringBuilder();
        for ( FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getDefaultMessage());
        }
        return ResponseMsgDto.setFail(HttpStatus.BAD_REQUEST.value(), sb.toString());
    }
}

