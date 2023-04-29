package com.sparta.myblog.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StatusEnum {

    // 200
    OK(HttpStatus.OK.value(), "OK"),

    // 400 BAD_REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST.value(), "토큰이 유효하지 않습니다."),
    INCORRECT_ADMIN_KEY(HttpStatus.BAD_REQUEST.value(), "ADMIN 정보가 일치하지 않습니다."),
    DUPLICATE_IDENTIFIER(HttpStatus.BAD_REQUEST.value(), "중복된 username 입니다."),
    NOT_AUTHORIZED_USER(HttpStatus.BAD_REQUEST.value(), "작성자만 수정,삭제할 수 있습니다."),

    // 404 NOT_FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "NOT_FOUND"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "회원을 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 게시글을 찾을 수 없습니다."),
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 댓글을 찾을 수 없습니다."),

    // 500
    INTERNAL_SERER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR");

    private final int status;
    private final String message;

    
}