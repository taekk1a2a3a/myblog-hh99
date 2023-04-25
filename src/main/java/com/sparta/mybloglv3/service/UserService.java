package com.sparta.mybloglv3.service;

import com.sparta.mybloglv3.dto.LoginRequestDto;
import com.sparta.mybloglv3.dto.MessageDto;
import com.sparta.mybloglv3.dto.SignupRequestDto;
import com.sparta.mybloglv3.entity.StatusEnum;
import com.sparta.mybloglv3.entity.Users;
import com.sparta.mybloglv3.jwt.JwtUtil;
import com.sparta.mybloglv3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    //회원가입
    @Transactional
    public ResponseEntity signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        //회원 중복 확인
        Optional<Users> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        Users user = new Users(username, password);
        userRepository.save(user);
        MessageDto messageDto = new MessageDto("회원 가입 성공!", StatusEnum.OK);
        return new ResponseEntity(messageDto, HttpStatus.OK);
    }

    //로그인
    @Transactional
    public ResponseEntity login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        Users user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        if(!user.getPassword().equals(password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));
        MessageDto messageDto = new MessageDto("로그인 성공!", StatusEnum.OK);
        return new ResponseEntity(messageDto, HttpStatus.OK);
    }
}