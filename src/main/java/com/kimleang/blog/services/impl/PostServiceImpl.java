package com.kimleang.blog.services.impl;

import com.github.slugify.Slugify;
import com.kimleang.blog.models.dtos.PostDto;
import com.kimleang.blog.models.entities.CategoryEntity;
import com.kimleang.blog.models.entities.ContentEntity;
import com.kimleang.blog.models.entities.PostEntity;
import com.kimleang.blog.models.entities.TagEntity;
import com.kimleang.blog.models.mappers.PostMapper;
import com.kimleang.blog.repositories.CategoryRepository;
import com.kimleang.blog.repositories.ContentRepository;
import com.kimleang.blog.repositories.PostRepository;
import com.kimleang.blog.repositories.TagRepository;
import com.kimleang.blog.services.PostService;
import com.kimleang.blog.utils.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService {

  private PostRepository postRepository;
  private CategoryRepository categoryRepository;
  private TagRepository tagRepository;
  private ContentRepository contentRepository;

  @Autowired
  public void setPostRepository(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Autowired
  public void setTagRepository(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  @Autowired
  public void setCategoryRepository(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Autowired
  public void setContentRepository(ContentRepository contentRepository) {
    this.contentRepository = contentRepository;
  }

  private Set<CategoryEntity> setupCategoryEntities(PostDto postDto) {
    Set<CategoryEntity> categories = new HashSet<>();
    postDto.getCategories().forEach(category -> {
      categories.add(categoryRepository.findByName(category.getName()));
    });
    return categories;
  }

  private Set<TagEntity> setupTagEntities(PostDto postDto) {
    Set<TagEntity> tags = new HashSet<>();
    postDto.getTags().forEach(tag -> {
      tags.add(tagRepository.findByName(tag.getName()));
    });
    return tags;
  }

  @Override
  public PostDto createPost(PostDto postDto) {
    Set<ContentEntity> contents = new HashSet<>();
    postDto.getContents().forEach(content -> {
      contents.add(
          new ContentEntity()
              .setName(content.getName())
              .setSlug(new Slugify().slugify(content.getName()))
      );
    });
    Set<CategoryEntity> categories = setupCategoryEntities(postDto);
    Set<TagEntity> tags = setupTagEntities(postDto);

    PostEntity postEntity = new PostEntity()
        .setTitle(postDto.getTitle())
        .setBody(postDto.getBody())
        .setCover(postDto.getCover())
        .setSlug(postDto.getSlug() + "-" + SequenceGenerator.generate(5))
        .setContents(contents)
        .setCategories(categories)
        .setTags(tags);
    try {
      postEntity = postRepository.save(postEntity);
    } catch (DataIntegrityViolationException ex) {
      return null;
    }
    return PostMapper.toPostDto(postEntity);
  }

  @Override
  public Set<PostDto> findAllPosts() {
    return PostMapper.toSetOfPostsDto(postRepository.findAll());
  }

  @Override
  public Page<PostEntity> findPostsByPaging(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return postRepository.findAll(pageable);
  }

  @Override
  @Transactional
  public PostDto findPostById(Long id) {
    Optional<PostEntity> postEntity = postRepository.findById(id);
    return postEntity.map(PostMapper::toPostDto).orElse(null);
  }

  @Override
  @Transactional
  public PostDto findPostBySlug(String slug) {
    Optional<PostEntity> postEntity = postRepository.findBySlug(slug);
    return postEntity.map(PostMapper::toPostDto).orElse(null);
  }

  @Override
  public PostDto updatePost(PostDto postDto, Long id) {
    Set<ContentEntity> contents = new HashSet<>();
    postDto.getContents().forEach(content -> contents.add(contentRepository.findByName(content.getName())));

    Set<CategoryEntity> categories = setupCategoryEntities(postDto);

    Set<TagEntity> tags = setupTagEntities(postDto);

    Optional<PostEntity> postEntity = postRepository.findById(id);
    if (postEntity.isPresent()) {
      PostEntity post = postEntity.get()
          .setTitle(postDto.getTitle())
          .setBody(postDto.getBody())
          .setSlug(postDto.getSlug())
          .setContents(contents)
          .setCategories(categories)
          .setTags(tags);
      try {
        post = postRepository.save(post);
      } catch (DataIntegrityViolationException ex) {
        post.setSlug(post.getSlug() + "-" + SequenceGenerator.generate(5));
        post = postRepository.save(post);
      }
      return PostMapper.toPostDto(post);
    } else {
      return null;
    }
  }

  @Override
  public PostDto deletePost(Long id) {
    Optional<PostEntity> postEntity = postRepository.findById(id);
    if (postEntity.isPresent()) {
      postRepository.deleteById(id);
      return PostMapper.toPostDto(postEntity.get());
    }
    return null;
  }

  @Override
  public PostDto publishPost(Long id) {
    Optional<PostEntity> post = postRepository.findById(id);
    if(post.isPresent()) {
      PostEntity postEntity = post.get();
      postEntity.setSavedDraft(false);
      postEntity = postRepository.save(postEntity);
      return PostMapper.toPostDto(postEntity);
    }
    return null;
  }
}
