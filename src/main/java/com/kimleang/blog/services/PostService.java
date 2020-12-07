package com.kimleang.blog.services;

import com.kimleang.blog.models.dtos.PostDto;
import com.kimleang.blog.models.entities.PostEntity;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface PostService {
  PostDto createPost(PostDto postDto);
  Set<PostDto> findAllPosts();
  Page<PostEntity> findPostsByPaging(int page, int size);
  PostDto findPostById(Long id);
  PostDto findPostBySlug(String slug);
  PostDto updatePost(PostDto postDto, Long id);
  PostDto deletePost(Long id);
  PostDto publishPost(Long id);
}
