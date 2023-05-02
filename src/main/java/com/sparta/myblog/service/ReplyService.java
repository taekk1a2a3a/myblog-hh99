package com.sparta.myblog.service;

import com.sparta.myblog.dto.ReplyResponseDto;
import com.sparta.myblog.dto.ResponseMsgDto;
import com.sparta.myblog.dto.ReplyRequestDto;
import com.sparta.myblog.entity.*;
import com.sparta.myblog.repository.ReplyRepository;
import com.sparta.myblog.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final Utils utils;

    //댓글 작성
    public ResponseMsgDto createReply(Long postId, ReplyRequestDto requestDto, Users user) {
        Post post = utils.findPostById(postId);
        Reply reply = new Reply(requestDto, user, post);
        replyRepository.save(reply);
        ReplyResponseDto replyResponseDto = new ReplyResponseDto(reply);
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "댓글 작성 완료", replyResponseDto);
    }

    //댓글 수정
    public ResponseMsgDto updateReply(Long replyId, ReplyRequestDto requestDto, Users user){
        Reply reply = utils.findReplyById(replyId);
        if (user.getRole().equals(UserRoleEnum.USER)){
            utils.isUserReply(user,reply);
        }
        reply.update(requestDto, user);
        ReplyResponseDto replyResponseDto = new ReplyResponseDto(reply);
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "댓글 수정 완료", replyResponseDto);
    }

    //댓글 삭제
    public ResponseMsgDto deleteReply(Long replyId, Users user){
        Reply reply = utils.findReplyById(replyId);
        if (user.getRole().equals(UserRoleEnum.USER)){
            utils.isUserReply(user,reply);
        }
        replyRepository.delete(reply);
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "댓글 삭제 완료", null);
    }
}
