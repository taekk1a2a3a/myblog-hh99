package com.sparta.myblog.jwt;

import com.sparta.myblog.dto.TokenDto;
import com.sparta.myblog.entity.RefreshToken;
import com.sparta.myblog.entity.UserRoleEnum;
import com.sparta.myblog.repository.RefreshTokenRepository;
import com.sparta.myblog.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtil {

    public static final String AUTHORIZATION_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";
    public static final String ACCESS_KEY = "ACCESS_KEY";
    public static final String REFRESH_KEY = "REFRESH_KEY";
    private static final Date ACCESS_TIME = (Date) Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
    private static final Date REFRESH_TIME = (Date) Date.from(Instant.now().plus(3, ChronoUnit.DAYS));
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes); //SHA256
    }

    // 액세스 토큰 및 리프레시 토큰 생성
    public TokenDto createAllToken(String username, UserRoleEnum role) {
        return new TokenDto(create(username, role, "Access"), create(username, role, "Refresh"));
    }

    // JWT 생성하는 메서드
    public String create(String username, UserRoleEnum role, String type) {
        Date date = new Date();
        Date exprTime = type.equals("Access") ? ACCESS_TIME : REFRESH_TIME;

        // JWT를 생성(Bearer Prefix를 넣어야 하는지?)
        return BEARER_PREFIX +
                Jwts.builder()
                        // 암호화에 사용될 알고리즘, 키
                        .signWith(SignatureAlgorithm.HS512, secretKey)
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setIssuedAt(date)
                        .setExpiration(exprTime)
                        .compact();
    }

    // header 토큰을 가져오기
    public String resolveToken(HttpServletRequest request, String token) {
        String tokenName = token.equals("ACCESS_KEY") ? ACCESS_KEY : REFRESH_KEY;
        //Authorization 이라는 헤더 값(토큰)을 가져옴
        String bearerToken = request.getHeader(tokenName);
        //토큰 값이 있는지, 토큰 값이 Bearer 로 시작하는지 판단
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            //Bearer를 자른 값을 전달
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        boolean result = false;
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return result;
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    //리프레시 토큰 검증
    public Boolean refreshTokenValidation(String token) {
        // 1차 토큰 검증
        if (!validateToken(token)) return false;

        // DB에 저장한 토큰 비교
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUsername(getUserInfoFromToken(token));

        return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken());
    }

    // username 을 토큰에서 추출
    public String getUserInfoFromToken(String token) {
        // 매개변수로 받은 token 을 키를 사용해서 복호화 (디코딩)
        // 복호화된 토큰의 payload 에서 subject 에 담긴 것을 가져옴
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    }

    //액세스 토큰 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(ACCESS_KEY, accessToken);
    }
}