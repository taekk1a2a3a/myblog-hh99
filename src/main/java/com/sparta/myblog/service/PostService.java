package com.sparta.myblog.service;

import com.sparta.myblog.dto.MessageDto;
import com.sparta.myblog.dto.PostRequestDto;
import com.sparta.myblog.dto.PostResponseDto;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.entity.StatusEnum;
import com.sparta.myblog.entity.UserRoleEnum;
import com.sparta.myblog.entity.Users;
import com.sparta.myblog.exception.CustomException;
import com.sparta.myblog.exception.ErrorCode;
import com.sparta.myblog.jwt.JwtUtil;
import com.sparta.myblog.repository.PostRepository;
import com.sparta.myblog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    //게시글 목록 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> getList() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostResponseDto::new).collect(Collectors.toList());
    }

    //선택한 게시글 조회
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id){
        Post post = findPostById(id);
        return new PostResponseDto(post);
    }

    //게시글 등록
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {
        String token = getToken(request);
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        Users user = findUser(claims);
        Post post = new Post(requestDto, user);
        return new PostResponseDto(postRepository.save(post));
    }

    //게시글 수정
    public PostResponseDto update(Long id, PostRequestDto requestDto, HttpServletRequest request) {
        String token = getToken(request);
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        Users user = findUser(claims);
        Post post = findPostById(id);
        if (user.getRole().equals(UserRoleEnum.USER)) {
            isUsersPost(user,post);
        }
        post.update(requestDto);
        return new PostResponseDto(post);
    }

    //게시글 삭제
    public ResponseEntity<MessageDto> deletePost(Long id, HttpServletRequest request) {
        String token = getToken(request);
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        Users user = findUser(claims);
        Post post = findPostById(id);
        if (user.getRole().equals(UserRoleEnum.USER)) {
            isUsersPost(user,post);
        }
        postRepository.deleteById(id);
        MessageDto messageDto = MessageDto.setSuccess(StatusEnum.OK.getStatusCode(), "게시글 삭제 완료", null);
        return new ResponseEntity(messageDto, HttpStatus.OK);
    }

    //게시글 확인
    public Post findPostById(Long id){
        return postRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }
    //사용자 확인
    public Users findUser(Claims claims){
        return  userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
    //토큰 확인
    public String getToken(HttpServletRequest request){
        String token = jwtUtil.resolveToken(request);
        if(token == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        if (!jwtUtil.validateToken(token)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        return token;
    }
    //작성자 게시물 확인
    public void isUsersPost(Users user, Post post){
        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED_USER);
        }
    }
}