package com.luxdone.file.dao;

import static com.luxdone.file.service.exception.LuxdoneExceptionMessage.NO_FILE_FOR_GIVEN_ID_MESSAGE;

import com.luxdone.file.mongo.model.FileMongo;
import com.luxdone.file.mongo.repository.MongoFileMetadataRepository;
import com.luxdone.file.mongo.repository.MongoFileRepository;
import com.luxdone.file.service.FileMapper;
import com.luxdone.file.service.exception.LuxdoneException;
import com.luxdone.file.service.model.FileData;
import com.luxdone.file.service.model.FileMetadata;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.bson.types.Binary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository to handle operations on mongo database.
 */
@AllArgsConstructor
@Repository
public class FileRepositoryImpl implements FileRepository {

  private final MongoFileMetadataRepository mongoFileMetadataRepository;
  private final MongoFileRepository mongoFileRepository;
  private final FileMapper fileMapper;

  @Override
  @Transactional
  public String saveFile(@NotNull FileData fileData) {
    var mongoFile = FileMongo.builder()
        .data(new Binary(fileData.getData()))
        .build();

    var fileId = mongoFileRepository.save(mongoFile).getId();
    var mongoMetadata = fileMapper.toMongo(fileData.getMetadata(), fileId);
    mongoFileMetadataRepository.save(mongoMetadata);
    return fileId;
  }

  @Override
  @Transactional
  public void updateFile(@NotNull FileData newFileData) {
    var updateMetadataMongo = fileMapper.toMongo(newFileData.getMetadata());
    var updateFileMongo = FileMongo.builder()
        .id(newFileData.getId())
        .data(new Binary(newFileData.getData()))
        .build();

    mongoFileMetadataRepository.save(updateMetadataMongo);
    mongoFileRepository.save(updateFileMongo);
  }

  @Override
  @Transactional
  public void removeFile(@NotNull String fileId) {
    mongoFileRepository.deleteById(fileId);
    mongoFileMetadataRepository.deleteByFileId(fileId);
  }

  @Override
  @Transactional
  public void removeAll() {
    mongoFileMetadataRepository.deleteAll();
    mongoFileRepository.deleteAll();
  }

  @Override
  public Optional<FileData> getFile(@NotNull String fileId) {
    var fileMongo = mongoFileRepository.findById(fileId)
        .orElseThrow(() -> new LuxdoneException(NO_FILE_FOR_GIVEN_ID_MESSAGE));
    var fileMetadataMongo = mongoFileMetadataRepository.findByFileId(fileId)
        .orElseThrow(() -> new LuxdoneException(NO_FILE_FOR_GIVEN_ID_MESSAGE));

    var fileMetadata = fileMapper.toDomain(fileMetadataMongo);

    var fileData = FileData.builder()
        .id(fileId)
        .data(fileMongo.getData().getData())
        .metadata(fileMetadata)
        .build();

    return Optional.of(fileData);
  }

  @Override
  public Optional<FileMetadata> getFileMetadata(@NotNull String fileId) {
    return mongoFileMetadataRepository
        .findByFileId(fileId)
        .map(fileMapper::toDomain);
  }

  @Override
  public List<FileMetadata> listFileMetadata() {
    return mongoFileMetadataRepository.findAll()
        .stream()
        .map(fileMapper::toDomain)
        .collect(Collectors.toList());
  }
}
