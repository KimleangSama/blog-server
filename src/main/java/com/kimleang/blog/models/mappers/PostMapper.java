package com.kimleang.blog.models.mappers;

import com.github.slugify.Slugify;
import com.kimleang.blog.models.dtos.CategoryDto;
import com.kimleang.blog.models.dtos.ContentDto;
import com.kimleang.blog.models.dtos.PostDto;
import com.kimleang.blog.models.dtos.TagDto;
import com.kimleang.blog.models.entities.PostEntity;

import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PostMapper {
  public static PostDto toPostDto(PostEntity postEntity) {
    return new PostDto()
        .setTitle(postEntity.getTitle())
        .setBody(new String(Base64.getDecoder().decode(postEntity.getBody())))
        .setSlug(postEntity.getSlug())
        .setCover(postEntity.getCover())
        .setContents(
            new HashSet<>(
                postEntity
                    .getContents()
                    .stream()
                    .map(content -> {
                      if (content != null)
                        return new ContentDto()
                            .setName(content.getName())
                            .setSlug(content.getSlug());
                      else return null;
                    })
                    .collect(Collectors.toSet())
            )
        )
        .setTags(
            new HashSet<>(
                postEntity
                    .getTags()
                    .stream()
                    .map(tag -> {
                      if (tag != null)
                        return new TagDto()
                            .setName(tag.getName())
                            .setSlug(tag.getSlug());
                      else return null;
                    })
                    .collect(Collectors.toSet())
            )
        )
        .setCategories(
            new HashSet<>(
                postEntity
                    .getCategories()
                    .stream()
                    .map(category -> {
                      if (category != null)
                        return new CategoryDto()
                            .setName(category.getName())
                            .setSlug(category.getSlug());
                      else return null;
                    })
                    .collect(Collectors.toSet())
            )
        );
  }

  public static Set<PostDto> toSetOfPostsDto(Set<PostEntity> postEntities) {
    return postEntities
        .stream()
        .map(PostMapper::toPostDto)
        .collect(Collectors.toSet());
  }

  public static Set<PostDto> toSetOfPostsDto(List<PostEntity> postEntities) {
    return postEntities
        .stream()
        .map(PostMapper::toPostDto)
        .collect(Collectors.toSet());
  }
}
