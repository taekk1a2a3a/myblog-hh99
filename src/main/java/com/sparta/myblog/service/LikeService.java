package com.sparta.myblog.service;

import com.sparta.myblog.dto.LikeDto;
import com.sparta.myblog.dto.ResponseMsgDto;
import com.sparta.myblog.entity.*;
import com.sparta.myblog.repository.PostLikeRepository;
import com.sparta.myblog.repository.ReplyLikeRepository;
import com.sparta.myblog.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final Utils utils;
    private final PostLikeRepository postLikeRepository;
    private final ReplyLikeRepository replyLikeRepository;

    // 게시글 좋아요
    public ResponseMsgDto postLikes(Long id, User user){
        Post post = utils.findPostById(id);
        Optional<PostLike> postLike = postLikeRepository.findByUserAndPost(user, post);
        if (postLike.isPresent()) {
            if (postLike.get().isDeleted()) {
                postLike.get().setDeleted(false);
                post.incLike();
            } else {
                postLike.get().setDeleted(true);
                post.decLike();
                return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "게시글 좋아요 취소", null);

            }
        } else {
            PostLike firstLike = new PostLike(user, post);
            postLikeRepository.save(firstLike);
            post.incLike();
        }
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "게시글 좋아요 ❤", null);
    }

    //게시글 좋아요 누른사람 조화
    @Transactional(readOnly = true)
    public ResponseMsgDto postLikesList(Long id) {
        Post post = utils.findPostById(id);
        List<LikeDto> likeDtoList = postLikeRepository.findAllByPost(post)
                .stream()
                .map(LikeDto::new)
                .collect(Collectors.toList());
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "이 게시글을 좋아하는 사람", likeDtoList);
    }

    // 댓글 좋아요
    public ResponseMsgDto replyLikes(Long id, User user){
        Reply reply = utils.findReplyById(id);
        Optional<ReplyLike> replyLike = replyLikeRepository.findByUserAndReply(user, reply);
        if (replyLike.isPresent()){
            if (replyLike.get().isDeleted()) {
                replyLike.get().setDeleted(false);
                reply.incLike();
            } else {
                replyLike.get().setDeleted(true);
                reply.decLike();
                return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "댓글 좋아요 취소", null);
            }
        } else {
            ReplyLike firstLike = new ReplyLike(user, reply);
            replyLikeRepository.save(firstLike);
            reply.incLike();
        }
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "댓글 좋아요 ❤", null);
    }

    //댓글 좋아요 누른사람 조화
    @Transactional(readOnly = true)
    public ResponseMsgDto replyLikesList(Long id) {
        Reply reply = utils.findReplyById(id);
        List<LikeDto> likeList = replyLikeRepository.findAllByReply(reply)
                .stream()
                .map(LikeDto::new)
                .collect(Collectors.toList());
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "이 댓글을 좋아하는 사람", likeList);
    }
}
