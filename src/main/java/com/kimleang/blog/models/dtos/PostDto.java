package com.kimleang.blog.models.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class PostDto {

  private String title;
  private String slug;
  private String body;
  private String cover;
  private boolean savedDraft;
  private Set<ContentDto> contents = new HashSet<>();
  private Set<TagDto> tags = new HashSet<>();
  private Set<CategoryDto> categories = new HashSet<>();

}
