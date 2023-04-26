package com.sparta.myblog.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final int statusCode;
    private final String message;

    public ErrorResponse(ErrorCode errorCode){
        this.statusCode = errorCode.getStatus().value();
        this.message = errorCode.getMessage();
    }
}
