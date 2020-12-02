package com.kimleang.blog.models.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts", uniqueConstraints = {
    @UniqueConstraint(columnNames = {
        "slug"
    })
})
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PostEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String title;
  private String slug;
  private String body;

  @OneToMany(
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  @JoinColumn(name = "fk_content")
  @ToString.Exclude
  private Set<ContentEntity> contents = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinTable(
      name = "posts_tags",
      joinColumns = {
          @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false, updatable = false)
      },
      inverseJoinColumns = {
          @JoinColumn(name = "tag_id", referencedColumnName = "id", nullable = false, updatable = false)
      }
  )
  private Set<TagEntity> tags = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  @JoinTable(
      name = "posts_categories",
      joinColumns = {
          @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false, updatable = false)
      },
      inverseJoinColumns = {
          @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false, updatable = false)
      }
  )
  @ToString.Exclude
  private Set<CategoryEntity> categories = new HashSet<>();

  private LocalDateTime createdAt = LocalDateTime.now();
  private LocalDateTime updatedAt = LocalDateTime.now();

}
