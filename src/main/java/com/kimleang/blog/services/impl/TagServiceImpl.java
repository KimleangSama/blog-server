package com.kimleang.blog.services.impl;

import com.kimleang.blog.models.dtos.TagDto;
import com.kimleang.blog.models.entities.PostEntity;
import com.kimleang.blog.models.entities.TagEntity;
import com.kimleang.blog.models.mappers.TagMapper;
import com.kimleang.blog.repositories.PostRepository;
import com.kimleang.blog.repositories.TagRepository;
import com.kimleang.blog.services.TagService;
import com.kimleang.blog.utils.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
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
  public TagDto createTag(TagDto tag) {
    TagEntity tagEntity = new TagEntity()
        .setName(tag.getName())
        .setSlug(tag.getName() + "-" + SequenceGenerator.generate(5));
    tagEntity = tagRepository.save(tagEntity);
    return TagMapper.toTagDto(tagEntity);
  }

  @Override
  public TagDto findTagById(Long id) {
    Optional<TagEntity> tag = tagRepository.findById(id);
    return tag.map(TagMapper::toTagDto).orElse(null);
  }

  @Override
  public TagDto updateTag(TagDto tagDto, Long id) {
    return null;
  }

  @Override
  public TagDto deleteTag(Long id) {
    Optional<TagEntity> tag = tagRepository.findById(id);
    if(tag.isPresent()) {
      tagRepository.deleteById(id);
      return TagMapper.toTagDto(tag.get());
    }
    return null;
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
