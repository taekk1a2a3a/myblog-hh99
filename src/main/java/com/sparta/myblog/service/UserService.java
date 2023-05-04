package com.sparta.myblog.service;

import com.sparta.myblog.dto.LoginRequestDto;
import com.sparta.myblog.dto.ResponseMsgDto;
import com.sparta.myblog.dto.SignupRequestDto;
import com.sparta.myblog.dto.TokenDto;
import com.sparta.myblog.entity.RefreshToken;
import com.sparta.myblog.entity.StatusEnum;
import com.sparta.myblog.entity.UserRoleEnum;
import com.sparta.myblog.entity.User;
import com.sparta.myblog.exception.CustomException;
import com.sparta.myblog.jwt.JwtUtil;
import com.sparta.myblog.repository.RefreshTokenRepository;
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

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;


    //회원가입
    @Transactional
    public ResponseMsgDto signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
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
        User user = new User(signupRequestDto, password);
        userRepository.save(user);

        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "회원가입 완료", null);
    }

    //로그인
    @Transactional
    public ResponseMsgDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 아이디 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(StatusEnum.USER_NOT_FOUND));
        // 패스워드 확인
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new CustomException(StatusEnum.USER_NOT_FOUND);
        }
        //username 정보로 Token 생성
        TokenDto tokenDto = jwtUtil.createAllToken(loginRequestDto.getUsername(), user.getRole());
        //Refresh 토큰 있는지 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUsername(loginRequestDto.getUsername());
        //Refresh 토큰이 있다면 새로 발급 후 업데이트
        //없다면 새로 만들고 DB에 저장
        if (refreshToken.isPresent()) {
            RefreshToken savedRefreshToken = refreshToken.get();
            RefreshToken updateToken = savedRefreshToken.updateToken(tokenDto.getRefreshToken().substring(7));
            refreshTokenRepository.save(updateToken);
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken().substring(7), username);
            refreshTokenRepository.save(newToken);
        }
        //응답 헤더에 토큰 추가
        response.addHeader(jwtUtil.ACCESS_KEY, tokenDto.getAccessToken());
        response.addHeader(jwtUtil.REFRESH_KEY, tokenDto.getRefreshToken());

        // 로그인 메세지 (관리자, 사용자)
        if(user.getRole().equals(UserRoleEnum.ADMIN)){
            return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "관리자 로그인 완료", null);
        }
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "사용자 로그인 완료", null);
    }

    //회원탈퇴
    public ResponseMsgDto deleteAccount(LoginRequestDto loginRequestDto, User user){
        String password = loginRequestDto.getPassword();
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new CustomException(StatusEnum.USER_NOT_FOUND);
        }
        userRepository.deleteById(user.getId());
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "회원 탈퇴 성공", null);
    }
}