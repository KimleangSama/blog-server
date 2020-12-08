package com.kimleang.blog.models.mappers;

import com.kimleang.blog.models.dtos.CategoryDto;
import com.kimleang.blog.models.dtos.PostDto;
import com.kimleang.blog.models.entities.CategoryEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryMapper {
  public static CategoryDto toCategoryDto(CategoryEntity categoryEntity) {
    return new CategoryDto()
        .setId(categoryEntity.getId())
        .setName(categoryEntity.getName())
        .setColor(categoryEntity.getColor())
        .setPosts(
            new HashSet<>(
                categoryEntity
                    .getPosts()
                    .stream()
                    .map(post -> new PostDto()
                        .setTitle(post.getTitle())
                        .setBody(post.getBody())
                        .setSlug(post.getSlug()))
                    .collect(Collectors.toSet())
            )
        );
  }

  public static Set<CategoryDto> toSetOfCategoriesDto(Set<CategoryEntity> categoryEntities) {
    return categoryEntities
        .stream()
        .map(CategoryMapper::toCategoryDto)
        .collect(Collectors.toSet());
  }

  public static Set<CategoryDto> toSetOfCategoriesDto(List<CategoryEntity> categoryEntities) {
    return categoryEntities
        .stream()
        .map(CategoryMapper::toCategoryDto)
        .collect(Collectors.toSet());
  }
}
