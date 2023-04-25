package com.sparta.myblog.dto;

import com.sparta.myblog.entity.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReplyReponseDto {
    private Long id;
    private String contents;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public ReplyReponseDto(Reply reply){
        this.id = reply.getId();
        this.contents = reply.getContents();
        this.username = reply.getUser().getUsername();
        this.createdAt = reply.getCreatedAt();
        this.modifiedAt = reply.getModifiedAt();
    }
}
