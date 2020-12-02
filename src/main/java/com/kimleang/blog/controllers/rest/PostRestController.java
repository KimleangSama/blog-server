package com.kimleang.blog.controllers.rest;

import com.github.slugify.Slugify;
import com.kimleang.blog.models.dtos.CategoryDto;
import com.kimleang.blog.models.dtos.ContentDto;
import com.kimleang.blog.models.dtos.PostDto;
import com.kimleang.blog.models.dtos.TagDto;
import com.kimleang.blog.models.entities.ContentEntity;
import com.kimleang.blog.models.entities.PostEntity;
import com.kimleang.blog.models.mappers.PostMapper;
import com.kimleang.blog.models.requests.PostRequest;
import com.kimleang.blog.models.responses.Response;
import com.kimleang.blog.repositories.PostRepository;
import com.kimleang.blog.services.PostService;
import com.kimleang.blog.utils.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(value = "${api.version}/posts")
public class PostRestController {

  private PostService postService;

  @Autowired
  public void setPostService(PostService postService) {
    this.postService = postService;
  }

  @GetMapping
  public String index() {
    Slugify slugify = new Slugify();


    return "KIMLEANG";
  }

  @PostMapping
  public Response<PostDto> savePost(@Valid @RequestBody PostRequest postRequest) {
    Slugify slugify = new Slugify();
    Set<ContentDto> contents = new HashSet<>();
    postRequest.getContentRequests().forEach(contentRequest -> {
      contents.add(
          new ContentDto()
              .setName(contentRequest.getName())
              .setSlug(contentRequest.getName())
      );
    });
    Set<TagDto> tags = new HashSet<>();
    postRequest.getTagRequests().forEach(tagRequest -> {
      tags.add(
          new TagDto()
              .setName(tagRequest.getName())
              .setSlug(tagRequest.getName())
      );
    });
    Set<CategoryDto> categories = new HashSet<>();
    postRequest.getCategoryRequests().forEach(categoryRequest -> {
      categories.add(
          new CategoryDto()
              .setName(categoryRequest.getName())
      );
    });
    PostDto postDto = new PostDto()
        .setTitle(postRequest.getTitle())
        .setBody(postRequest.getBody())
        .setSlug(slugify.slugify(postRequest.getTitle()))
        .setContents(contents)
        .setTags(tags)
        .setCategories(categories);
    try {
      postDto = postService.createPost(postDto);
    } catch (DataIntegrityViolationException ex) {
      return Response.<PostDto>exception()
          .setMessage("Duplicate entry " + postDto.getSlug() + " for key slug.")
          .setData(null)
          .setCode(500);
    }
    return Response.<PostDto>ok("You have created a post successfully.").setData(postDto);
  }

  @GetMapping("/{id}")
  public Response<PostDto> getPostById(@PathVariable(name = "id") Long id) {
    try {
      PostDto postDto = postService.findPostById(id);
      if (postDto != null) {
        return Response.<PostDto>ok("You get post with Id: " + id + " success.").setData(postDto);
      } else {
        return Response.<PostDto>notFound("You get post with Id: " + id + " failed.").setData(null);
      }
    } catch (Exception ex) {
      return Response.<PostDto>exception()
          .setData(null)
          .setMessage(ex.getLocalizedMessage());
    }
  }

  @GetMapping("/{slug}")
  public Response<PostDto> getPostById(@PathVariable(name = "slug") String slug) {
    try {
      PostDto postDto = postService.findPostBySlug(slug);
      if (postDto != null) {
        return Response.<PostDto>ok("You get post with Slug: " + slug + " success.").setData(postDto);
      } else {
        return Response.<PostDto>notFound("You get post with Slug: " + slug + " failed.").setData(null);
      }
    } catch (Exception ex) {
      return Response.<PostDto>exception()
          .setData(null)
          .setMessage(ex.getLocalizedMessage());
    }
  }

}
