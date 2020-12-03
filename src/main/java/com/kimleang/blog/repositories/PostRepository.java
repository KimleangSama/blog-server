package com.kimleang.blog.repositories;

import com.kimleang.blog.models.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
  Optional<PostEntity> findBySlug(String slug);
}
