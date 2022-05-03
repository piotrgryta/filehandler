package com.luxdone.file.service.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FileData {

  String id;
  byte[] data;
  FileMetadata metadata;

}
