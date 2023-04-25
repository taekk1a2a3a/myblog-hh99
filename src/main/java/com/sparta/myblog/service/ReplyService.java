package com.sparta.myblog.service;

import com.sparta.myblog.dto.MessageDto;
import com.sparta.myblog.dto.ReplyReponseDto;
import com.sparta.myblog.dto.ReplyRequestDto;
import com.sparta.myblog.entity.*;
import com.sparta.myblog.jwt.JwtUtil;
import com.sparta.myblog.repository.PostRepository;
import com.sparta.myblog.repository.ReplyRepository;
import com.sparta.myblog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.MediaSize;
import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final JwtUtil jwtUtil;

    //댓글 작성
    public ReplyReponseDto createReply(Long postId, ReplyRequestDto requestDto, HttpServletRequest request) {
        String token = getToken(request);
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        Users user = findUser(claims);
        Post post = findPostById(postId);
        Reply reply = new Reply(requestDto, user, post);
        return new ReplyReponseDto(replyRepository.save(reply));
    }

    //댓글 수정
    public ReplyReponseDto updateReply(Long replyId, ReplyRequestDto requestDto, HttpServletRequest request){
        String token = getToken(request);
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        Users user = findUser(claims);
        Reply reply = findReplyById(replyId);
        if (user.getRole().equals(UserRoleEnum.USER)){
            isUserReply(user,reply);
        }
        reply.update(requestDto, user);
        return new ReplyReponseDto(reply);
    }
    //댓글 삭제
    public ResponseEntity<MessageDto> deleteReply(Long replyId, HttpServletRequest request){
        String token = getToken(request);
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        Users user = findUser(claims);
        Reply reply = findReplyById(replyId);
        if (user.getRole().equals(UserRoleEnum.USER)){
            isUserReply(user,reply);
        }
        replyRepository.delete(reply);
        MessageDto messageDto = MessageDto.setSuccess(StatusEnum.OK.getStatusCode(), "댓글 삭제 완료", null);
        return new ResponseEntity(messageDto, HttpStatus.OK);
    }


    //----
    //게시글 확인
    public Post findPostById(Long id){
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }
    //사용자 확인
    public Users findUser(Claims claims){
        return  userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
    }
    //토큰 확인
    public String getToken(HttpServletRequest request){
        String token = jwtUtil.resolveToken(request);
        if(token == null) {
            throw new NoSuchElementException("올바르지 않은 접근입니다.");
        }
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }
        return token;
    }
    //댓글 확인
    public Reply findReplyById(Long id){
        return replyRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
    }
    //댓글 주인 확인
    public void isUserReply(Users user, Reply reply){
        if (!reply.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("작성자만 수정, 삭제 가능합니다.");
        }
    }
}
