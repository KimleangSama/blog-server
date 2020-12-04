package com.kimleang.blog.utils;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Paging {
  private int page;
  private int size;
  private int totalPages;
  private int totalSizes;
}
