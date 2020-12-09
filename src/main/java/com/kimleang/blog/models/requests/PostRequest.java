package com.kimleang.blog.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;


@Data
public class PostRequest {
  private String title;
  private String body;
  private String cover;
  @JsonProperty(value = "categories")
  private Set<CategoryRequest> categoryRequests;
  @JsonProperty(value = "tags")
  private Set<TagRequest> tagRequests;
}
