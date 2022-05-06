package com.luxdone.file.service;

import static com.luxdone.file.service.exception.LuxdoneExceptionMessage.NO_FILE_FOR_GIVEN_ID_MESSAGE;
import static com.luxdone.file.service.exception.LuxdoneExceptionMessage.UPDATE_NOT_POSSIBLE_MESSAGE;
import static com.luxdone.file.service.exception.LuxdoneExceptionMessage.WRONG_FILE_BYTES_MESSAGE;

import com.luxdone.file.dao.FileRepository;
import com.luxdone.file.service.exception.LuxdoneException;
import com.luxdone.file.service.model.FileData;
import com.luxdone.file.service.model.FileMetadata;
import com.luxdone.file.service.util.MetadataExtractor;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for handling files operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

  private final FileRepository fileRepository;

  /**
   * Save new multipart file to database.
   *
   * @param file - multipart file
   * @return id of a file
   */
  public String saveFile(MultipartFile file) {
    log.debug("Saving file: {}", file.getOriginalFilename());
    FileData fileData;
    try {
      fileData = FileData.builder()
          .data(file.getBytes())
          .metadata(MetadataExtractor.extractMetadata(file))
          .build();
    } catch (IOException e) {
      throw new LuxdoneException(WRONG_FILE_BYTES_MESSAGE);
    }
    log.debug("File data: {}", fileData);
    return fileRepository.saveFile(fileData);
  }

  /**
   * Update file of the same extension as before.
   * If different extension is provided, exception is thrown.
   *
   * @param file   - multipart file
   * @param fileId - id of a file to be updated
   */
  public void updateFile(MultipartFile file, String fileId) {
    log.debug("Updating file of id: {} to {}", fileId, file.getOriginalFilename());
    var existingFileData = fileRepository.getFile(fileId)
        .orElseThrow(() -> new LuxdoneException(NO_FILE_FOR_GIVEN_ID_MESSAGE));
    var existingMetadata = existingFileData.getMetadata();
    var extractedMetadata = MetadataExtractor.extractMetadata(file);
    var newFileMetadata = new FileMetadata(extractedMetadata, existingMetadata.getId(), fileId);
    FileData newFileData;
    if (isFileExtensionMatched(newFileMetadata, existingFileData.getMetadata())) {
      try {
        newFileData = FileData.builder()
            .id(fileId)
            .data(file.getBytes())
            .metadata(newFileMetadata)
            .build();
      } catch (IOException e) {
        throw new LuxdoneException(WRONG_FILE_BYTES_MESSAGE);
      }
    } else {
      throw new LuxdoneException(UPDATE_NOT_POSSIBLE_MESSAGE);
    }
    log.debug("File data: {}", newFileData);
    fileRepository.updateFile(newFileData);
  }

  /**
   * Get full metadata information.
   *
   * @return list of metadata information
   */
  public List<FileMetadata> listMetadata() {
    log.debug("Listing metadata");
    return fileRepository.listFileMetadata();
  }

  public FileMetadata getFileMetadata(String fileId) {
    log.debug("Getting metadata for id: {}", fileId);
    return fileRepository.getFileMetadata(fileId)
        .orElseThrow(() -> new LuxdoneException(NO_FILE_FOR_GIVEN_ID_MESSAGE));
  }

  /**
   * Get full file data for file id.
   *
   * @param fileId id of a file
   * @return full file data
   */
  public FileData getFile(String fileId) {
    log.debug("Getting file by id: {}", fileId);
    return fileRepository.getFile(fileId)
        .orElseThrow(() -> new LuxdoneException(NO_FILE_FOR_GIVEN_ID_MESSAGE));
  }

  /**
   * Remove a file by specific id.
   *
   * @param fileId - id of a file to be removed
   */
  public void removeFile(String fileId) {
    log.debug("Removing file by id: {}", fileId);
    fileRepository.removeFile(fileId);
  }

  /**
   * Clear all databases
   */
  public void removeAllFiles() {
    log.debug("Removing all files");
    fileRepository.removeAll();
  }

  private boolean isFileExtensionMatched(FileMetadata f1, FileMetadata f2) {
    return f1.getExtension().equals(f2.getExtension());
  }

}
