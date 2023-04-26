package com.sparta.myblog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //400 BAD_REQUEST
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),
    INCORRECT_ADMIN_KEY(HttpStatus.BAD_REQUEST, "ADMIN 정보가 일치하지 않습니다."),
    DUPLICATE_IDENTIFIER(HttpStatus.BAD_REQUEST, "중복된 username 입니다."),
    NOT_AUTHORIZED_USER(HttpStatus.BAD_REQUEST, "작성자만 수정,삭제할 수 있습니다."),
    //404 NOT_FOUND,
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

}
