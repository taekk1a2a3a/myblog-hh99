package com.sparta.myblog.controller;

import com.sparta.myblog.dto.LoginRequestDto;
import com.sparta.myblog.dto.ResponseMsgDto;
import com.sparta.myblog.dto.SignupRequestDto;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @DeleteMapping("/delete")
    public ResponseMsgDto deleteAccount(@RequestBody LoginRequestDto loginRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.deleteAccount(loginRequestDto, userDetails.getUser());
    }
}