package com.kimleang.blog.services.impl;

import com.kimleang.blog.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

  private final String serverPath;
  private final Path root;

  @Autowired
  public FileStorageServiceImpl(@Value("${file.upload.server.path}") String serverPath) {
    this.serverPath = serverPath;
    this.root = Paths.get(serverPath);
  }

  @Override
  public void init() {
    try {
      Files.createDirectory(root);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  @Override
  public List<String> multipleSave(List<MultipartFile> files){
    List<String> fileDownloadUri = new ArrayList<>();
    if(!files.isEmpty()) {
      files.forEach(file -> {
        fileDownloadUri.add(this.save(file));
      });
    }
    return fileDownloadUri;
  }

  @Override
  public String save(MultipartFile file) {
    String filename = "";
    if(!file.isEmpty()) {
      filename = UUID.randomUUID() + "." + Objects.requireNonNull(file.getOriginalFilename())
          .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
      try {
        Files.copy(file.getInputStream(), Paths.get(serverPath, filename));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return filename;
  }

  @Override
  public Resource load(String filename) {
    try {
      Path file = root.resolve(filename);
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(root.toFile());
  }

  @Override
  public Stream<Path> loadAll() {
    try {
      return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not load the files!");
    }
  }

}
