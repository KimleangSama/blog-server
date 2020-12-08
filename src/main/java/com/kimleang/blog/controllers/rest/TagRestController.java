package com.kimleang.blog.controllers.rest;

import com.kimleang.blog.models.dtos.PostDto;
import com.kimleang.blog.models.dtos.TagDto;
import com.kimleang.blog.models.requests.TagRequest;
import com.kimleang.blog.models.responses.Response;
import com.kimleang.blog.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("${api.version}/tags")
public class TagRestController {

  private TagService tagService;

  @Autowired
  public void setTagService(TagService tagService) {
    this.tagService = tagService;
  }

  @PostMapping
  public Response<TagDto> createTag(@Valid @RequestBody TagRequest tagRequest) {
    TagDto tagDto = new TagDto()
        .setName(tagRequest.getName())
        .setColor(randomBadgeColor());
    try {
      tagDto = tagService.createTag(tagDto);
    } catch (DataIntegrityViolationException ex) {
      return Response.<TagDto>exception()
          .setMessage("Duplicate entry " + tagDto.getName() + " for key name.")
          .setData(null)
          .setCode(500);
    }
    return Response.<TagDto>ok("You have created a post successfully.").setData(tagDto);
  }

  private String randomBadgeColor() {
    String[] colors = {"primary", "secondary", "success", "danger", "warning", "info", "light", "dark"};
    return colors[(int)(Math.random() * colors.length)];
  }

  @GetMapping
  public Response<Set<TagDto>> getAllTags() {
    Set<TagDto> tags = tagService.getAllTags();
    if (tags == null || tags.isEmpty()) {
      return Response.<Set<TagDto>>notFound("There are no tags.").setData(null);
    }
    return Response.<Set<TagDto>>ok("You have retrieved all tags.").setData(tags);
  }

  @GetMapping("/{id}")
  public Response<TagDto> getTagsById(@PathVariable("id") Long id) {
    TagDto tag = tagService.findTagById(id);
    if (tag == null) {
      return Response.<TagDto>notFound("There are no tags.").setData(null);
    }
    return Response.<TagDto>ok("You have retrieved all tags.").setData(tag);
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
