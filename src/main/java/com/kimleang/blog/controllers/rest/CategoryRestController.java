package com.kimleang.blog.controllers.rest;

import com.kimleang.blog.models.dtos.CategoryDto;
import com.kimleang.blog.models.dtos.TagDto;
import com.kimleang.blog.models.requests.CategoryRequest;
import com.kimleang.blog.models.requests.TagRequest;
import com.kimleang.blog.models.responses.Response;
import com.kimleang.blog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("${api.version}/categories")
public class CategoryRestController {

  private CategoryService categoryService;

  @Autowired
  public void setCategoryService(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @PostMapping
  public Response<CategoryDto> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
    CategoryDto categoryDto = new CategoryDto()
        .setName(categoryRequest.getName());
    try {
      categoryDto = categoryService.createCategory(categoryDto);
    } catch (DataIntegrityViolationException ex) {
      return Response.<CategoryDto>exception()
          .setMessage("Duplicate entry " + categoryDto.getName() + " for key name.")
          .setData(null)
          .setCode(500);
    }
    return Response.<CategoryDto>ok("You have created a categories successfully.").setData(categoryDto);
  }

  @GetMapping
  public Response<Set<CategoryDto>> getAllTags() {
    Set<CategoryDto> categories = categoryService.getAllCategories();
    if (categories == null || categories.isEmpty()) {
      return Response.<Set<CategoryDto>>notFound("There are no categories.").setData(null);
    }
    return Response.<Set<CategoryDto>>ok("You have retrieved all categories.").setData(categories);
  }

}
