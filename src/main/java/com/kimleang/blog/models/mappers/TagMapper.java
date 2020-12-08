package com.kimleang.blog.models.mappers;

import com.kimleang.blog.models.dtos.PostDto;
import com.kimleang.blog.models.dtos.TagDto;
import com.kimleang.blog.models.entities.TagEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TagMapper {
  public static TagDto toTagDto(TagEntity tagEntity) {
    return new TagDto()
        .setId(tagEntity.getId())
        .setName(tagEntity.getName())
        .setColor(tagEntity.getColor())
        .setPosts(
            new HashSet<>(
                tagEntity
                    .getPosts()
                    .stream()
                    .map(post -> new PostDto()
                        .setTitle(post.getTitle())
                        .setBody(post.getBody())
                        .setSlug(post.getSlug()))
                    .collect(Collectors.toSet())
            )
        )
        ;
  }

  public static Set<TagDto> toSetOfTagsDto(Set<TagEntity> tagEntities) {
    return tagEntities
        .stream()
        .map(TagMapper::toTagDto)
        .collect(Collectors.toSet());
  }
}
