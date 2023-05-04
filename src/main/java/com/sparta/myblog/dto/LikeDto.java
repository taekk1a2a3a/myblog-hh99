package com.sparta.myblog.dto;

import com.sparta.myblog.entity.PostLike;
import com.sparta.myblog.entity.ReplyLike;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeDto {
    String username;

    public LikeDto(PostLike postLike){
        this.username = postLike.getUser().getUsername();
    }
    public LikeDto(ReplyLike replyLike) {this.username = replyLike.getUser().getUsername(); }
}
