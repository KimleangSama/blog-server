package com.kimleang.blog.services.impl;

import com.github.slugify.Slugify;
import com.kimleang.blog.models.dtos.PostDto;
import com.kimleang.blog.models.dtos.TagDto;
import com.kimleang.blog.models.entities.CategoryEntity;
import com.kimleang.blog.models.entities.ContentEntity;
import com.kimleang.blog.models.entities.PostEntity;
import com.kimleang.blog.models.entities.TagEntity;
import com.kimleang.blog.models.mappers.PostMapper;
import com.kimleang.blog.repositories.CategoryRepository;
import com.kimleang.blog.repositories.PostRepository;
import com.kimleang.blog.repositories.TagRepository;
import com.kimleang.blog.services.PostService;
import com.kimleang.blog.utils.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

  private PostRepository postRepository;
  private CategoryRepository categoryRepository;
  private TagRepository tagRepository;

  @Autowired
  public void setPostRepository(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Autowired
  public void setTagRepository(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  @Autowired
  public void setCategoryRepository(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public PostDto createPost(PostDto postDto) {
    Set<ContentEntity> contents = new HashSet<>();
    postDto.getContents().forEach(content -> {
      contents.add(
          new ContentEntity()
              .setName(content.getName())
              .setSlug(new Slugify().slugify(content.getName()))
      );
    });
    Set<CategoryEntity> categories = new HashSet<>();
    postDto.getCategories().forEach(category -> {
      categories.add(categoryRepository.findByName(category.getName()));
    });
    Set<TagEntity> tags = new HashSet<>();
    postDto.getTags().forEach(tag -> {
      tags.add(tagRepository.findByName(tag.getName()));
    });

    PostEntity postEntity = new PostEntity()
        .setTitle(postDto.getTitle())
        .setBody(postDto.getBody())
        .setSlug(postDto.getSlug())
        .setContents(contents)
        .setCategories(categories)
        .setTags(tags);
    try {
      postEntity = postRepository.save(postEntity);
    } catch (DataIntegrityViolationException ex) {
      postEntity.setSlug(postEntity.getSlug() + "-" + SequenceGenerator.generate(5));
      postEntity = postRepository.save(postEntity);
    }
    return PostMapper.toPostDto(postEntity);
  }

  @Override
  public List<PostDto> findAll() {
    return null;
  }

  @Override
  public PostDto findPostById(Long id) {
    return null;
  }

  @Override
  public PostDto findPostBySlug(String slug) {
    return PostMapper.toPostDto(postRepository.findBySlug(slug));
  }

  @Override
  public PostDto updatePost(PostDto postDto) {
    return null;
  }

  @Override
  public PostDto deletePost(Long id) {
    return null;
  }
}
