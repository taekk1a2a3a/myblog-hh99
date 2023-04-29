package com.sparta.myblog.util;

import com.sparta.myblog.entity.Post;
import com.sparta.myblog.entity.Reply;
import com.sparta.myblog.entity.StatusEnum;
import com.sparta.myblog.entity.Users;
import com.sparta.myblog.exception.CustomException;
import com.sparta.myblog.repository.PostRepository;
import com.sparta.myblog.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Utils {

    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;

    //게시글 확인
    public Post findPostById(Long id){
        return postRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusEnum.POST_NOT_FOUND));
    }
    //작성자 게시물 확인
    public void isUsersPost(Users user, Post post){
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
    public void isUserReply(Users user, Reply reply){
        if (!reply.getUser().getId().equals(user.getId())) {
            throw new CustomException(StatusEnum.NOT_AUTHORIZED_USER);
        }
    }
}
