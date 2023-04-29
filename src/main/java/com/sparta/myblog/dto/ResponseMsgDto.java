package com.sparta.myblog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // (staticName = "add")
public class ResponseMsgDto<T> {
    private int statusCode;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> ResponseMsgDto<T> setSuccess(int statusCode, String message, T data){
        return new ResponseMsgDto<>(statusCode, message, data);
    }

    public static <T> ResponseMsgDto<T> setFail(int statusCode, String message){
        return new ResponseMsgDto<>(statusCode, message, null);
    }
}