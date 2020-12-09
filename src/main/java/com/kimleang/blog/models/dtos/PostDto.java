package com.kimleang.blog.models.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class PostDto {

  private Long id;
  private String title;
  private String slug;
  private String body;
  private String cover;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private boolean savedDraft;
  private Set<TagDto> tags = new HashSet<>();
  private Set<CategoryDto> categories = new HashSet<>();

}
