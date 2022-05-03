package com.luxdone.file.service.model;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class FileMetadata {

  @NotNull
  String id;
  @NotNull
  String fileId;
  @NotNull
  String originalName;
  @NotNull
  String extension;
  @NotNull
  String mediaType;
  @NotNull
  Long size;

  public FileMetadata(FileMetadata fileMetadata, String id, String fileId) {
    this.id = id;
    this.fileId = fileId;
    this.originalName = fileMetadata.getOriginalName();
    this.extension = fileMetadata.getExtension();
    this.mediaType = fileMetadata.getMediaType();
    this.size = fileMetadata.getSize();
  }
}
