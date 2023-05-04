package com.sparta.myblog.util;

import com.sparta.myblog.entity.*;
import com.sparta.myblog.exception.CustomException;
import com.sparta.myblog.repository.PostRepository;
import com.sparta.myblog.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class Utils {

    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;

    //게시글 확인
    public Post findPostById(Long id){
        return postRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusEnum.POST_NOT_FOUND));
    }
    //작성자 게시물 확인
    public void isUsersPost(User user, Post post){
        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomException(StatusEnum.NOT_AUTHORIZED_USER);
        }
    }

    //댓글 확인
    public Reply findReplyById(Long id){
        return replyRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusEnum.REPLY_NOT_FOUND));
        }
    //댓글 주인 확인
    public void isUserReply(User user, Reply reply){
        if (!reply.getUser().getId().equals(user.getId())) {
            throw new CustomException(StatusEnum.NOT_AUTHORIZED_USER);
        }
    }
}
