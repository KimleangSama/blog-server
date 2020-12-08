package com.kimleang.blog.services.impl;

import com.kimleang.blog.models.dtos.CategoryDto;
import com.kimleang.blog.models.entities.CategoryEntity;
import com.kimleang.blog.models.mappers.CategoryMapper;
import com.kimleang.blog.repositories.CategoryRepository;
import com.kimleang.blog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {

  private CategoryRepository categoryRepository;

  @Autowired
  public void setCategoryRepository(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public CategoryDto createCategory(CategoryDto categoryDto) {
    CategoryEntity categoryEntity = new CategoryEntity()
        .setName(categoryDto.getName())
        .setColor(randomBadgeColor());
    categoryEntity = categoryRepository.save(categoryEntity);
    return CategoryMapper.toCategoryDto(categoryEntity);
  }

  private String randomBadgeColor() {
    String[] colors = {"primary", "secondary", "success", "danger", "warning", "info", "light", "dark"};
    return colors[(int)(Math.random() * colors.length)];
  }

  @Override
  public CategoryDto findCategoryById(Long id) {
    return null;
  }

  @Override
  public CategoryDto updateCategory(CategoryDto categoryDto, Long id) {
    return null;
  }

  @Override
  public CategoryDto deleteCategory(Long id) {
    return null;
  }

  @Override
  public Set<CategoryDto> getAllCategories() {
    List<CategoryEntity> categoryEntities = categoryRepository.findAll();
    return CategoryMapper.toSetOfCategoriesDto(categoryEntities);
  }

  @Override
  public Set<CategoryDto> getAllCategoriesByPostSlug(String slug) {
    return null;
  }
}
