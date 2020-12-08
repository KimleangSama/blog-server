package com.kimleang.blog.services;

import com.kimleang.blog.models.dtos.CategoryDto;
import com.kimleang.blog.models.dtos.TagDto;

import java.util.Set;

public interface CategoryService {
  CategoryDto createCategory(CategoryDto tag);
  CategoryDto findCategoryById(Long id);
  CategoryDto updateCategory(CategoryDto tagDto, Long id);
  CategoryDto deleteCategory(Long id);
  Set<CategoryDto> getAllCategories();
  Set<CategoryDto> getAllCategoriesByPostSlug(String slug);
}
