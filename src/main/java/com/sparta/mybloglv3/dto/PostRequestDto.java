package com.sparta.mybloglv3.dto;

import lombok.Getter;

@Getter
public class PostRequestDto {
    private String username;
    private String title;
    private String contents;
}