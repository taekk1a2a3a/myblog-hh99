package com.sparta.myblog.exception;

import com.sparta.myblog.entity.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private final StatusEnum statusEnum;
}
