package com.sparta.myblog.service;

import com.sparta.myblog.dto.ResponseMsgDto;
import com.sparta.myblog.entity.*;
import com.sparta.myblog.repository.LikeRepository;
import com.sparta.myblog.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final Utils utils;
    private final LikeRepository likeRepository;

    // 게시글 좋아요
    public ResponseMsgDto postLikes(Long id, User user){
        Post post = utils.findPostById(id);
        Optional<Like> likes = likeRepository.findByUserAndPost(user, post);
        if (likes.isPresent()){
            utils.clickLikes(likes, post);
            if (likes.get().isDeleted()){return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "게시글 좋아요 취소", null);}
        } else {
            Like firstLike = new Like(user, post);
            likeRepository.save(firstLike);
            post.incLike();
        }
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "게시글 좋아요 ❤", null);
    }

    //게시글 좋아요 누른사람 조화
    @Transactional(readOnly = true)
    public ResponseMsgDto postLikesList(Long id) {
        Post post = utils.findPostById(id);
        List<Like> likeList = likeRepository.findAllByPost(post);
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "이 게시글을 좋아하는 사람", likeList);
    }

    // 댓글 좋아요
    public ResponseMsgDto replyLikes(Long id, User user){
        Reply reply = utils.findReplyById(id);
        Optional<Like> likes = likeRepository.findByUserAndReply(user,reply);
        if (likes.isPresent()){
            utils.clickLikes(likes, reply);
            if (likes.get().isDeleted()){return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "댓글 좋아요 취소", null);}
        } else {
            Like firstLike = new Like(user, reply);
            likeRepository.save(firstLike);
            reply.incLike();
        }
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "댓글 좋아요 ❤", null);
    }

    //댓글 좋아요 누른사람 조화
    @Transactional(readOnly = true)
    public ResponseMsgDto replyLikesList(Long id) {
        Reply reply = utils.findReplyById(id);
        List<Like> likeList = likeRepository.findAllByReply(reply);
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "이 댓글을 좋아하는 사람", likeList);
    }
}
