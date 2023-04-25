package com.sparta.myblog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageDto<T> {
    private int statusCode;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> MessageDto<T> setSuccess(int statusCode, String message, T data){
        return new MessageDto<>(statusCode, message, data);
    }

    public static <T> MessageDto<T> setFail(int statusCode, String message){
        return new MessageDto<>(statusCode, message, null);
    }
}