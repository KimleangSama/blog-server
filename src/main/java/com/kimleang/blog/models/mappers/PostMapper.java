package com.kimleang.blog.models.mappers;

import com.github.slugify.Slugify;
import com.kimleang.blog.models.dtos.CategoryDto;
import com.kimleang.blog.models.dtos.ContentDto;
import com.kimleang.blog.models.dtos.PostDto;
import com.kimleang.blog.models.dtos.TagDto;
import com.kimleang.blog.models.entities.PostEntity;

import java.util.HashSet;
import java.util.stream.Collectors;

public class PostMapper {
  public static PostDto toPostDto(PostEntity postEntity) {
    Slugify slugify = new Slugify();
    return new PostDto()
        .setTitle(postEntity.getTitle())
        .setBody(postEntity.getBody())
        .setSlug(slugify.slugify(postEntity.getTitle()))
        .setContents(
            new HashSet<>(
                postEntity
                    .getContents()
                    .stream()
                    .map(content -> new ContentDto()
                        .setName(content.getName())
                        .setSlug(content.getSlug()))
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
}
