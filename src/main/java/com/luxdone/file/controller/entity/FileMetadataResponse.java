package com.luxdone.file.controller.entity;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FileMetadataResponse {

  @NotNull
  String fileId;
  @NotNull
  String originalName;
  @NotNull
  String extension;
  @NotNull
  Long size;


}
