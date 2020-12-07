package com.kimleang.blog.services;

import com.kimleang.blog.models.dtos.TagDto;

import java.util.Set;

public interface TagService {
  TagDto createTag(TagDto tag);
  TagDto findTagById(Long id);
  TagDto updateTag(TagDto tagDto, Long id);
  TagDto deleteTag(Long id);
  Set<TagDto> getAllTags();
  Set<TagDto> getAllTagsByPostSlug(String slug);
}
