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
import com.sparta.myblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

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
    public PostResponseDto createPost(PostRequestDto requestDto, Users user) {
        Post post = new Post(requestDto, user);
        return new PostResponseDto(postRepository.save(post));
    }

    //게시글 수정
    public PostResponseDto update(Long id, PostRequestDto requestDto, Users user) {

        Post post = findPostById(id);
        if (user.getRole().equals(UserRoleEnum.USER)) {
            isUsersPost(user,post);
        }
        post.update(requestDto);
        return new PostResponseDto(post);
    }

    //게시글 삭제
    public ResponseEntity<MessageDto> deletePost(Long id, Users user) {

        Post post = findPostById(id);
        if (user.getRole().equals(UserRoleEnum.USER)) {
            isUsersPost(user,post);
        }
        postRepository.delete(post);
        MessageDto messageDto = MessageDto.setSuccess(StatusEnum.OK.getStatusCode(), "게시글 삭제 완료", null);
        return new ResponseEntity(messageDto, HttpStatus.OK);
    }

    //게시글 확인
    public Post findPostById(Long id){
        return postRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    //작성자 게시물 확인
    public void isUsersPost(Users user, Post post){
        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED_USER);
        }
    }
}