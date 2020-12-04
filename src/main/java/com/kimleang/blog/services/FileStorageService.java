package com.kimleang.blog.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FileStorageService {
  void init();

  String save(MultipartFile file);

  List<String> multipleSave(List<MultipartFile> files) throws IOException;

  Resource load(String filename);

  void deleteAll();

  Stream<Path> loadAll();
}
