package com.kimleang.blog.controllers.rest;

import com.kimleang.blog.models.responses.FileInfoResponse;
import com.kimleang.blog.models.responses.FileUploadResponse;
import com.kimleang.blog.models.responses.Response;
import com.kimleang.blog.services.impl.FileStorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.version}/files")
public class FileUploadRestController {

//  @Value(value = "${file.upload.server.path}")
//  private String serverPath;

  @Value("${file.base.url}")
  private String imageUrl;

  private FileStorageServiceImpl fileStorageService;

  @Autowired()
  public void setFileStorageService(FileStorageServiceImpl fileStorageService) {
    this.fileStorageService = fileStorageService;
  }

  @PostMapping("/upload")
  public ResponseEntity<Map<String, Object>> upload(@RequestParam("file") MultipartFile file) {
    Map<String, Object> res = new HashMap<>();
    String fileName;
    try {
      fileName = fileStorageService.save(file);
      res.put("message", "Uploaded the file successfully");
      res.put("status", true);
      res.put("data", (imageUrl + fileName));
    } catch (Exception e) {
      res.put("Could not upload the file: ", file.getOriginalFilename() + "!");
      res.put("message", "Could not upload the file:");
      res.put("status", false);
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(res);
    }
    return ResponseEntity.status(HttpStatus.OK).body(res);
  }


  @PostMapping("/multiple-upload")
  public Object multipleUpload(@RequestParam("files") MultipartFile[] files) {
    Map<String, Object> res = new HashMap<>();
    try {
      List<String> saved = fileStorageService.multipleSave(Arrays.asList(files));
      res.put("message", "Uploaded the file successfully");
      res.put("status", true);
      res.put("data", saved.stream().map(save -> imageUrl + save));
      return ResponseEntity.status(HttpStatus.OK).body(res);
    } catch (Exception e) {
      e.printStackTrace();
      res.put("message", "Could not upload the file: ");
      res.put("status", false);
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(res);
    }
  }

  @GetMapping("/download/{name}")
  public ResponseEntity<Resource> downloadFile(@PathVariable("name") String fileName, HttpServletRequest request) {
    Resource resource = null;
    if (fileName != null && !fileName.isEmpty()) {
      try {
        resource = fileStorageService.load(fileName);
      } catch (Exception e) {
        e.printStackTrace();
      }
      String contentType = null;
      try {
        assert resource != null;
        contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
      } catch (IOException ignored) {
      }
      if (contentType == null) {
        contentType = "application/octet-stream";
      }
      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(contentType))
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
          .body(resource);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping
  public ResponseEntity<List<FileInfoResponse>> getListFiles() {
    List<FileInfoResponse> fileInfos = fileStorageService.loadAll().map(path -> {
      String filename = path.getFileName().toString();
      String url = MvcUriComponentsBuilder
          .fromMethodName(FileUploadRestController.class, "getFile",
              path.getFileName().toString()).build().toString();
      return new FileInfoResponse(filename, url);
    }).collect(Collectors.toList());
    System.out.println(fileInfos.listIterator());
    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
  }

  @GetMapping("/{filename:.+}")
  public ResponseEntity<Resource> getFile(@PathVariable String filename) {
    Resource file = fileStorageService.load(filename);
    System.out.println(file);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

}
