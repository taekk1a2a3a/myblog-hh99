package com.sparta.myblog.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myblog.dto.ResponseMsgDto;
import com.sparta.myblog.entity.User;
import com.sparta.myblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //헤더의 토큰 가져오기
        String access_token = jwtUtil.resolveToken(request, jwtUtil.ACCESS_KEY);
        String refresh_token = jwtUtil.resolveToken(request, jwtUtil.REFRESH_KEY);

        //// 액세스 토큰 존재 여부 판단
        if(access_token != null) {
            // 엑세스 토큰 유효성 검사
            if(jwtUtil.validateToken(access_token)){
                setAuthentication(jwtUtil.getUserInfoFromToken(access_token));
            }
            // 토큰 만료 && 리스페시 토큰이 존재
            else if (refresh_token != null && jwtUtil.refreshTokenValidation(refresh_token)) {
                boolean isRefreshToken = jwtUtil.refreshTokenValidation(refresh_token);
                // 리프레시 토큰으로 username, Member DB 에서 username 을 가진 member 가져오기
                String username = jwtUtil.getUserInfoFromToken(refresh_token);
                User user = userRepository.findByUsername(username).get();
                // 새로운 액세스 토큰 발급
                String newAccessToken = jwtUtil.create(username, user.getRole(), "Access");
                // 헤더에 액세스 토큰 추가
                jwtUtil.setHeaderAccessToken(response, newAccessToken);
                // Security context 에 인증 정보 넣기
                setAuthentication(username);
            } else if (refresh_token == null) {
                jwtExceptionHandler(response, HttpStatus.BAD_REQUEST.value(), "AccessToken Expired. Please send the refresh Token together");
                return;
            }
            // (토큰 만료 && 리프레시 토큰 만료) || 리프레시 토큰이 DB와 비교했을 때 같지 않다면
            else {
                jwtExceptionHandler(response, HttpStatus.BAD_REQUEST.value(),"RefreshToken Expired");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // SecurityContext 에 Authentication 객체를 저장
    public void setAuthentication(String username) {
        Authentication authentication = jwtUtil.createAuthentication(username);
        // security 가 만들어주는 securityContextHolder 그 안에 authentication 을 넣음
        // security 가 securityContextHolder 에서 인증 객체를 확인
        // JwtAuthenticationFilter 에서 authentication 을 넣어주면 UsernamePasswordAuthenticationFilter 내부에서 인증이 된 것을 확인하고 추가적인 작업을 진행하지 않음
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // 예외 처리 핸들러
    public void jwtExceptionHandler(HttpServletResponse response, int statusCode, String msg) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(ResponseMsgDto.setFail(statusCode, msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}