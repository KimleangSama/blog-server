package com.kimleang.blog.services;

import com.kimleang.blog.models.dtos.PostDto;

import java.util.List;

public interface PostService {
  PostDto createPost(PostDto postDto);
  List<PostDto> findAll();
  PostDto findPostById(Long id);
  PostDto findPostBySlug(String slug);
  PostDto updatePost(PostDto postDto);
  PostDto deletePost(Long id);
}
