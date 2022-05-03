package com.luxdone.file.service.util;

import com.luxdone.file.service.model.FileMetadata;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@UtilityClass
public class MetadataExtractor {

  public FileMetadata extractMetadata(MultipartFile file) {
    var originalName = file.getOriginalFilename();
    var mediaType = file.getContentType();
    var extension = StringUtils.getFilenameExtension(originalName);
    var size = file.getSize();

    return FileMetadata.builder()
        .originalName(originalName)
        .extension(extension)
        .mediaType(mediaType)
        .size(size)
        .build();
  }

}
