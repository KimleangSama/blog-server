package com.kimleang.blog.models.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CategoryDto {
  private String name;
  private String color;
}
