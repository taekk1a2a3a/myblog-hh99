package com.sparta.myblog.service;

import com.sparta.myblog.dto.PostRequestDto;
import com.sparta.myblog.dto.PostResponseDto;
import com.sparta.myblog.dto.ResponseMsgDto;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.entity.StatusEnum;
import com.sparta.myblog.entity.UserRoleEnum;
import com.sparta.myblog.entity.Users;
import com.sparta.myblog.repository.PostRepository;
import com.sparta.myblog.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final Utils utils;

    //게시글 목록 조회
    @Transactional(readOnly = true)
    public ResponseMsgDto<List<PostResponseDto>> getList() {
         List<PostResponseDto> postList = postRepository.findAllByOrderByCreatedAtDesc()
                 .stream()
                 .map(PostResponseDto::new)
                 .collect(Collectors.toList());
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "게시글 목록 조회", postList);
    }

    //선택한 게시글 조회
    @Transactional(readOnly = true)
    public ResponseMsgDto getPost(Long id){
        Post post = utils.findPostById(id);
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "게시글 조회", postResponseDto);
    }

    //게시글 등록
    public ResponseMsgDto createPost(PostRequestDto requestDto, Users user) {
        Post post = new Post(requestDto, user);
        postRepository.save(post);
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "게시글 등록 완료", postResponseDto);
    }

    //게시글 수정
    public ResponseMsgDto update(Long id, PostRequestDto requestDto, Users user) {

        Post post = utils.findPostById(id);
        if (user.getRole().equals(UserRoleEnum.USER)) {
            utils.isUsersPost(user,post);
        }
        post.update(requestDto);
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "게시글 수정 완료", postResponseDto);
    }

    //게시글 삭제
    public ResponseMsgDto deletePost(Long id, Users user) {

        Post post = utils.findPostById(id);
        if (user.getRole().equals(UserRoleEnum.USER)) {
            utils.isUsersPost(user,post);
        }
        postRepository.delete(post);
        return ResponseMsgDto.setSuccess(StatusEnum.OK.getStatus(), "게시글 삭제 완료", null);
    }
}