package com.kimleang.blog.models.requests;

import lombok.Data;

import java.util.Set;


@Data
public class PostRequest {
  private String title;
  private String body;
  private Set<ContentRequest> contentRequests;
  private Set<CategoryRequest> categoryRequests;
  private Set<TagRequest> tagRequests;
}
