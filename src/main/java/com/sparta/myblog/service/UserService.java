package com.sparta.myblog.service;

import com.sparta.myblog.dto.LoginRequestDto;
import com.sparta.myblog.dto.ResponseMsgDto;
import com.sparta.myblog.dto.SignupRequestDto;
import com.sparta.myblog.entity.StatusEnum;
import com.sparta.myblog.entity.UserRoleEnum;
import com.sparta.myblog.entity.Users;
import com.sparta.myblog.exception.CustomException;
import com.sparta.myblog.jwt.JwtUtil;
import com.sparta.myblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    //회원가입
    @Transactional
    public ResponseMsgDto signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 회원 중복 확인
        Optional<Users> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new CustomException(StatusEnum.DUPLICATE_IDENTIFIER);
        }
        // 회원 Role 확인
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new CustomException(StatusEnum.INCORRECT_ADMIN_KEY);
            }
            signupRequestDto.setRole(UserRoleEnum.ADMIN);
        }
        Users user = new Users(signupRequestDto, password);
        userRepository.save(user);

        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "회원가입 완료", null);
    }

    //로그인
    @Transactional
    public ResponseMsgDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 아이디 확인
        Users user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(StatusEnum.USER_NOT_FOUND));
        // 패스워드 확인
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new CustomException(StatusEnum.USER_NOT_FOUND);
        }
        // 토큰 추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));

        // 로그인 메세지 (관리자, 사용자)
        if(user.getRole().equals(UserRoleEnum.ADMIN)){
            return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "관리자 로그인 완료", null);
        }
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "사용자 로그인 완료", null);
    }
}