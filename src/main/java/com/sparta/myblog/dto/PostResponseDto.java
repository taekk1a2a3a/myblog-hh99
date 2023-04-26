package com.sparta.myblog.dto;

import com.sparta.myblog.entity.Post;
import com.sparta.myblog.entity.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String username;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<ReplyReponseDto> replyList = new ArrayList<>();

    public PostResponseDto(Post post){
        this.id = post.getId();
        this.username = post.getUser().getUsername();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.replyList = post.getReplyList()
                .stream()
                .sorted(Comparator.comparing(Reply::getCreatedAt).reversed())
                .map(ReplyReponseDto::new)
                .collect(Collectors.toList());
    }
}