package com.sparta.mybloglv3.controller;

import com.sparta.mybloglv3.dto.MessageDto;
import com.sparta.mybloglv3.dto.PostRequestDto;
import com.sparta.mybloglv3.dto.PostResponseDto;
import com.sparta.mybloglv3.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //게시글 목록 조회
    @GetMapping("/posts")
    public List<PostResponseDto> getList() {
        return postService.getList();
    }

    //선택한 게시글 조회
    @GetMapping("/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    //게시글 등록
    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.createPost(requestDto, request);
    }

    //게시글 수정
    @PutMapping("/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request){
        return postService.update(id, requestDto, request);
    }

    //게시글 삭제
    @DeleteMapping("/post/{id}")
    public MessageDto deletePost(@PathVariable Long id, HttpServletRequest request){
        return postService.deletePost(id, request);
    }
}