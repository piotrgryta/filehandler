package com.luxdone.file.mongo.repository;

import com.luxdone.file.mongo.model.FileMetadataMongo;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoFileMetadataRepository extends MongoRepository<FileMetadataMongo, String> {
  Optional<FileMetadataMongo> findByFileId(String fileId);

  void deleteByFileId(String fileId);
}
