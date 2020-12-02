package com.kimleang.blog.repositories;

import com.kimleang.blog.models.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
  CategoryEntity findByName(String name);
}
