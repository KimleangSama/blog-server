package com.kimleang.blog.models.mappers;

import com.kimleang.blog.models.dtos.ContentDto;
import com.kimleang.blog.models.entities.ContentEntity;
import org.springframework.stereotype.Component;


public class ContentMapper {
  public static ContentDto toContentDto(ContentEntity contentEntity) {
    return new ContentDto();
//        .set;
  }

}
