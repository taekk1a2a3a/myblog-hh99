package com.sparta.myblog.controller;

import com.sparta.myblog.dto.LoginRequestDto;
import com.sparta.myblog.dto.ResponseMsgDto;
import com.sparta.myblog.dto.SignupRequestDto;
import com.sparta.myblog.entity.StatusEnum;
import com.sparta.myblog.exception.CustomException;
import com.sparta.myblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseMsgDto signup(@RequestBody @Valid SignupRequestDto signupRequestDto){
        return userService.signup(signupRequestDto);
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseMsgDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        return userService.login(loginRequestDto, response);
    }
}