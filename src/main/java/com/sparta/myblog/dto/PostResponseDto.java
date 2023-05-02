package com.sparta.myblog.dto;

import com.sparta.myblog.entity.Post;
import com.sparta.myblog.entity.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private int likes;
    private List<Reply> replyList;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public PostResponseDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContents();
        this.username = post.getUser().getUsername();
        this.likes = post.getLikes();
        this.replyList = post.getReplyList();
        this.createAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}
