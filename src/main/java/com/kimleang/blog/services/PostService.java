package com.kimleang.blog.services;

import com.kimleang.blog.models.dtos.PostDto;
import com.kimleang.blog.models.entities.PostEntity;
import org.springframework.data.domain.Page;

public interface PostService {
  PostDto createPost(PostDto postDto);
  Page<PostEntity> findPostsByPaging(int page, int size);
  PostDto findPostById(Long id);
  PostDto findPostBySlug(String slug);
  PostDto updatePost(PostDto postDto, Long id);
  PostDto deletePost(Long id);
}
