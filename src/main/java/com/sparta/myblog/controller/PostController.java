package com.sparta.myblog.controller;

import com.sparta.myblog.dto.PostRequestDto;
import com.sparta.myblog.dto.ResponseMsgDto;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //게시글 목록 조회
    @GetMapping("/posts")
    public ResponseMsgDto getList() {
        return postService.getList();
    }

    //선택한 게시글 조회
    @GetMapping("/post/{id}")
    public ResponseMsgDto getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    //게시글 등록
    @PostMapping("/post")
    public ResponseMsgDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(requestDto, userDetails.getUser());
    }

    //게시글 수정
    @PutMapping("/post/{id}")
    public ResponseMsgDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.update(id, requestDto, userDetails.getUser());
    }

    //게시글 삭제
    @DeleteMapping("/post/{id}")
    public ResponseMsgDto deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.deletePost(id, userDetails.getUser());
    }
}