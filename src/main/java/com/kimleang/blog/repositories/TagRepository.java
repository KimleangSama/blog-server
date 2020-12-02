package com.kimleang.blog.repositories;

import com.kimleang.blog.models.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {
  TagEntity findByName(String name);
}
