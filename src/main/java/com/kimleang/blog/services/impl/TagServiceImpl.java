package com.kimleang.blog.services.impl;

import com.kimleang.blog.models.dtos.TagDto;
import com.kimleang.blog.models.entities.PostEntity;
import com.kimleang.blog.models.mappers.TagMapper;
import com.kimleang.blog.repositories.PostRepository;
import com.kimleang.blog.repositories.TagRepository;
import com.kimleang.blog.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

  private TagRepository tagRepository;
  private PostRepository postRepository;

  @Autowired
  public void setTagRepository(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  @Autowired
  public void setPostRepository(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Override
  @Transactional
  public Set<TagDto> getAllTags() {
    return TagMapper.toSetOfTagsDto(new HashSet<>(tagRepository.findAll()));
  }

  @Override
  public Set<TagDto> getAllTagsByPostSlug(String slug) {
    Optional<PostEntity> postEntity = postRepository.findBySlug(slug);
    System.out.println(postEntity.toString());
    return TagMapper.toSetOfTagsDto(postEntity.get().getTags());
//    return TagMapper.toSetOfTagsDto(tagRepository.findAllTagsBySlug(slug));
  }

}
