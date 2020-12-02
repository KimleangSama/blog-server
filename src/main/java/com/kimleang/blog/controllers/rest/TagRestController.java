package com.kimleang.blog.controllers.rest;

import com.kimleang.blog.models.dtos.TagDto;
import com.kimleang.blog.models.responses.Response;
import com.kimleang.blog.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("${api.version}/tags")
public class TagRestController {

  private TagService tagService;

  @Autowired
  public void setTagService(TagService tagService) {
    this.tagService = tagService;
  }

  @GetMapping
  public Response<Set<TagDto>> getAllTags() {
    Set<TagDto> tags = tagService.getAllTags();
    if (tags == null || tags.isEmpty()) {
      return Response.<Set<TagDto>>notFound("There are no tags.").setData(null);
    }
    return Response.<Set<TagDto>>ok("You have retrieved all tags.").setData(tags);
  }

  @GetMapping("/get-tags-by-slug/{slug}")
  public Response<Set<TagDto>> getTagsBySlug(@PathVariable("slug") String slug) {
    Set<TagDto> tags = tagService.getAllTagsByPostSlug(slug);
    if (tags == null || tags.isEmpty()) {
      return Response.<Set<TagDto>>notFound("There are no tags.").setData(null);
    }
    return Response.<Set<TagDto>>ok("You have retrieved all tags.").setData(tags);
  }

}
