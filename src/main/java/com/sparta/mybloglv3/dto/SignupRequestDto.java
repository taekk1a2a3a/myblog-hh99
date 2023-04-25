package com.sparta.mybloglv3.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignupRequestDto {
    @Size(min = 4, max = 10, message ="아이디의 길이는 4자 이상, 10자 이하이어야 합니다.")
    @Pattern(regexp = "[a-z0-9]*$", message = "아이디는 소문자와 숫자로만 구성되어야 합니다.")
    private String username;

    @Size(min=8, max= 15, message="비밀번호의 길이는 8자에서 15자 사이입니다")
    @Pattern(regexp = "[a-zA-Z0-9`~!@#$%^&*()_=+|{};:,.<>/?]*$",message = "비밀번호 형식이 일치하지 않습니다")
    private String password;
}