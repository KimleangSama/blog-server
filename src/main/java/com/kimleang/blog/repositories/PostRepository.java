package com.kimleang.blog.repositories;

import com.kimleang.blog.models.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
  PostEntity findBySlug(String slug);

}
