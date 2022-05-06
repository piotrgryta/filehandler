package com.luxdone.file.controller;

import com.luxdone.file.controller.entity.FileMetadataResponse;
import com.luxdone.file.service.FileMapper;
import com.luxdone.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
@Tag(name = "File controller", description = "Controller for files management")
public class FileController {

  private final FileService fileService;
  private final FileMapper fileMapper;

  @PostMapping(consumes = "multipart/form-data")
  @Operation(summary = "Save any file",
      description = "Save any file and its metadata ")
  public ResponseEntity<String> saveFile(
      @RequestParam("file") @Valid @NotNull MultipartFile file) {
    
    var fileId = fileService.saveFile(file);
    return ResponseEntity.ok(fileId);
  }

  @GetMapping("/metadata")
  @Operation(summary = "Get all stored metadata information",
      description = "Gets all metadata information")
  public ResponseEntity<List<FileMetadataResponse>> getAllMetadataInfo() {
    return ResponseEntity.ok(
        fileService.listMetadata().stream()
            .map(fileMapper::toResponse)
            .collect(Collectors.toList())
    );
  }

  @GetMapping("/metadata/{fileId}")
  @Operation(summary = "Get specific metadata information",
      description = "Gets metadata information for specific id")
  public ResponseEntity<FileMetadataResponse> getMetadataForId(@PathVariable String fileId) {
    return ResponseEntity.ok(
        fileMapper.toResponse(fileService.getFileMetadata(fileId)));
  }

  @GetMapping("/{fileId}")
  @Operation(summary = "Show file",
      description = "Show file by provided file id")
  public ResponseEntity<InputStreamResource> getFile(@PathVariable String fileId) {
    var fileData = fileService.getFile(fileId);

    var metadata = fileData.getMetadata();

    var resource = new InputStreamResource(new ByteArrayInputStream(fileData.getData()));
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + metadata.getOriginalName());

    var mediaType = MediaType.parseMediaType(metadata.getMediaType());

    return ResponseEntity.ok()
        .headers(headers)
        .contentLength(metadata.getSize())
        .contentType(mediaType)
        .body(resource);
  }

  @PutMapping(path = "/{fileId}", consumes = "multipart/form-data")
  @Operation(summary = "Update a file",
      description = "File has to be of the same type")
  public ResponseEntity<String> updateFile(@RequestParam("file") @NotNull MultipartFile file,
                                           @PathVariable String fileId) {

    fileService.updateFile(file, fileId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{fileId}")
  public ResponseEntity<String> removeFile(@PathVariable String fileId) {
    fileService.removeFile(fileId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  @Operation(summary = "Clear all data",
      description = "Clears all data and metadata")
  public ResponseEntity<String> removeAll() {
    fileService.removeAllFiles();
    return ResponseEntity.ok().build();
  }

}
