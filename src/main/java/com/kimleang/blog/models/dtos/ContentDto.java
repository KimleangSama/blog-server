package com.kimleang.blog.models.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class ContentDto {
  private String name;
  private String slug;
  private Set<PostDto> posts = new HashSet<>();
}
