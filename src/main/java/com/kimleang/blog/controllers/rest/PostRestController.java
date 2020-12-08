package com.kimleang.blog.controllers.rest;

import com.github.slugify.Slugify;
import com.kimleang.blog.models.dtos.CategoryDto;
import com.kimleang.blog.models.dtos.PostDto;
import com.kimleang.blog.models.dtos.TagDto;
import com.kimleang.blog.models.entities.PostEntity;
import com.kimleang.blog.models.mappers.PostMapper;
import com.kimleang.blog.models.requests.PostRequest;
import com.kimleang.blog.models.responses.Response;
import com.kimleang.blog.services.PostService;
import com.kimleang.blog.utils.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "${api.version}/posts")
public class PostRestController {

  private PostService postService;

  @Autowired
  public void setPostService(PostService postService) {
    this.postService = postService;
  }

  @PostMapping
  public Response<PostDto> savePost(@Valid @RequestBody PostRequest postRequest) {
    PostDto postDto = setupPostRequest(postRequest);
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

  @GetMapping("/all")
  public Response<Set<PostDto>> getAllPosts() {
    try {
      Set<PostDto> posts = postService.findAllPosts();
      return Response.<Set<PostDto>>ok("You retrieved").setData(posts);
    } catch (Exception ex) {
      return Response.<Set<PostDto>>exception().setData(null).setPaging(null);
    }
  }

  @GetMapping
  public Response<Set<PostDto>> getAllPostsByPaging(@RequestParam int page, @RequestParam int size) {
    try {
      Page<PostEntity> postEntityPage = postService.findPostsByPaging(page, size);
      Set<PostDto> posts = PostMapper.toSetOfPostsDto(postEntityPage.getContent());
      Paging paging = new Paging()
          .setPage(page)
          .setSize(size)
          .setTotalPages(postEntityPage.getTotalPages())
          .setTotalSizes((int) postEntityPage.getTotalElements());
      return Response.<Set<PostDto>>ok("You retrieved").setData(posts).setPaging(paging);
    } catch (Exception ex) {
      return Response.<Set<PostDto>>exception().setData(null).setPaging(null);
    }
  }

  @GetMapping("/id/{id}")
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
  public Response<PostDto> getPostBySlug(@PathVariable(name = "slug") String slug) {
    try {
      PostDto postDto = postService.findPostBySlug(slug);
      if (postDto != null) {
        return Response.<PostDto>ok("You get post with Slug: " + slug + " success.").setData(postDto);
      } else {
        return Response.<PostDto>notFound("You get post with Slug: " + slug + " failed.").setData(null);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      return Response.<PostDto>exception()
          .setData(null)
          .setMessage(ex.getLocalizedMessage());
    }
  }

  @PatchMapping("/update/{id}")
  public Response<PostDto> updatePost(@Valid @RequestBody PostRequest postRequest, @PathVariable long id) {
    PostDto postDto = setupPostRequest(postRequest);
    try {
      postDto = postService.updatePost(postDto, id);
    } catch (DataIntegrityViolationException ex) {
      return Response.<PostDto>exception()
          .setMessage("Duplicate entry " + postDto.getSlug() + " for key slug.")
          .setData(null)
          .setCode(500);
    }
    return Response.<PostDto>ok("You have created a post successfully.").setData(postDto);
  }

  @DeleteMapping("/{id}")
  public Response<PostDto> getPostBySlug(@PathVariable(name = "id") Long id) {
    try {
      PostDto postDto = postService.deletePost(id);
      if (postDto != null) {
        return Response.<PostDto>ok("You delete post with Id: " + id + " success.").setData(postDto);
      } else {
        return Response.<PostDto>notFound("You delete post with Id: " + id + " failed.").setData(null);
      }
    } catch (Exception ex) {
      return Response.<PostDto>exception()
          .setData(null)
          .setMessage(ex.getLocalizedMessage());
    }
  }

  @PutMapping("/{id}/published")
  public Response<PostDto> publishedPost(@PathVariable("id") long id) {
    try {
      PostDto postDto = postService.publishPost(id);
      return Response.<PostDto>ok("").setData(postDto);
    } catch (Exception ex) {
      ex.printStackTrace();
      return Response.<PostDto>exception()
          .setData(null)
          .setMessage(ex.getLocalizedMessage());
    }
  }

  private PostDto setupPostRequest(PostRequest postRequest) {
    Slugify slugify = new Slugify();
    Set<TagDto> tags = new HashSet<>();
    if (postRequest.getTagRequests() != null)
      postRequest.getTagRequests().forEach(tagRequest -> tags.add(
          new TagDto()
              .setName(tagRequest.getName())
      ));
    Set<CategoryDto> categories = new HashSet<>();
    if (postRequest.getCategoryRequests() != null)
      postRequest.getCategoryRequests().forEach(categoryRequest -> categories.add(
          new CategoryDto()
              .setName(categoryRequest.getName())
      ));
    return new PostDto()
        .setTitle(postRequest.getTitle())
        .setBody(postRequest.getBody())
        .setSlug(slugify.slugify(postRequest.getTitle()))
        .setCover(postRequest.getCover())
        .setTags(tags)
        .setCategories(categories);
  }

}
