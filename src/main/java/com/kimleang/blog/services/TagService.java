package com.kimleang.blog.services;

import com.kimleang.blog.models.dtos.TagDto;

import java.util.Set;

public interface TagService {
  Set<TagDto> getAllTags();
  Set<TagDto> getAllTagsByPostSlug(String slug);
}
