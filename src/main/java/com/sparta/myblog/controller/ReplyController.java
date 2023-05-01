package com.sparta.myblog.controller;

import com.sparta.myblog.dto.ReplyRequestDto;
import com.sparta.myblog.dto.ResponseMsgDto;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;

    //댓글 작성
    @PostMapping("/{post-id}")
    public ResponseMsgDto createReply(@PathVariable(name = "post-id") Long postId, @RequestBody ReplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return replyService.createReply(postId, requestDto, userDetails.getUser());
    }
    //댓글 수정
    @PutMapping("/{reply-id}")
    public ResponseMsgDto updateReply(@PathVariable(name = "reply-id") Long replyId, @RequestBody ReplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return replyService.updateReply(replyId, requestDto, userDetails.getUser());
    }
    //댓글 삭제
    @DeleteMapping("/{reply-id}")
    public ResponseMsgDto deleteReply(@PathVariable(name = "reply-id") Long replyId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return replyService.deleteReply(replyId, userDetails.getUser());
    }

}
