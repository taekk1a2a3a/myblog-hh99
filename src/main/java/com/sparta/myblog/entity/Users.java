package com.sparta.myblog.entity;

import com.sparta.myblog.dto.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING) // STRING : 열거형 상수의 이름을 데이터베이스에 저장합니다. ORDINAL : 열거형 상수의 순서 값을 데이터베이스에 저장합니다.
    private UserRoleEnum role;

    public Users(SignupRequestDto signupRequestDto) {
        this.username = signupRequestDto.getUsername();
        this.password = signupRequestDto.getPassword();
        this.role = signupRequestDto.getRole();
    }
}