package com.sparta.myblog.controller;

import com.sparta.myblog.dto.ResponseMsgDto;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    //게시글 좋아요
    @PostMapping("like/post/{post-id}")
    public ResponseMsgDto postLikes(@PathVariable(name = "post-id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likeService.postLikes(id, userDetails.getUser());
    }
    //게시글 좋아요 조회
    @GetMapping("/like/post/{post-id}")
    public ResponseMsgDto postLikesList(@PathVariable(name = "post-id") Long id){
        return likeService.postLikesList(id);
    }

    //댓글 좋아요
    @PostMapping("/like/reply/{reply-id}")
    public ResponseMsgDto replyLikes(@PathVariable(name = "reply-id") Long replyId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likeService.replyLikes(replyId, userDetails.getUser());
    }
    //댓글 좋아요 조회
    @GetMapping("/like/reply/{reply-id}")
    public ResponseMsgDto replyLikesList(@PathVariable(name = "reply-id") Long replyId){
        return likeService.replyLikesList(replyId);
    }
}
