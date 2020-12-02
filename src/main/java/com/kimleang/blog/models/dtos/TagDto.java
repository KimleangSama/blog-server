package com.kimleang.blog.models.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class TagDto {
  private String name;
  private String slug;
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<PostDto> posts = new HashSet<>();
}
