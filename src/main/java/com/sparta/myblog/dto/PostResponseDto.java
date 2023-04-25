package com.sparta.myblog.dto;

import com.sparta.myblog.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String username;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PostResponseDto(Post post){
        this.id = post.getId();
        this.username = post.getUser().getUsername();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }

}