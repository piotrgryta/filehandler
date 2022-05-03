package com.luxdone.file.service;

import static org.mapstruct.ReportingPolicy.IGNORE;

import com.luxdone.file.controller.entity.FileMetadataResponse;
import com.luxdone.file.mongo.model.FileMetadataMongo;
import com.luxdone.file.service.model.FileMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(unmappedTargetPolicy = IGNORE, componentModel = "spring")
public interface FileMapper {

  @Mapping(source = "fileId", target = "fileId")
  FileMetadataMongo toMongo(FileMetadata fileMetadata, String fileId);

  FileMetadataMongo toMongo(FileMetadata fileMetadata);

  FileMetadata toDomain(FileMetadataMongo fileMetadata);

  FileMetadataResponse toResponse(FileMetadata fileMetadata);

}
