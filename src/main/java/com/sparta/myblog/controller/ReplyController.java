package com.sparta.myblog.controller;

import com.sparta.myblog.dto.MessageDto;
import com.sparta.myblog.dto.ReplyReponseDto;
import com.sparta.myblog.dto.ReplyRequestDto;
import com.sparta.myblog.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;

    //댓글 작성
    @PostMapping("/{postId}")
    public ReplyReponseDto createReply(@PathVariable Long postId, @RequestBody ReplyRequestDto requestDto, HttpServletRequest request){
        return replyService.createReply(postId, requestDto, request);
    }
    //댓글 수정
    @PutMapping("/{replyId}")
    public ReplyReponseDto updateReply(@PathVariable Long replyId, @RequestBody ReplyRequestDto requestDto, HttpServletRequest request){
        return replyService.updateReply(replyId, requestDto, request);
    }
    //댓글 삭제
    @DeleteMapping("/{replyId}")
    public ResponseEntity<MessageDto> deleteReply(@PathVariable Long replyId, HttpServletRequest request){
        return replyService.deleteReply(replyId, request);
    }

}
