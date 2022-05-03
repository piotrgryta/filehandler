package com.luxdone.file.dao;

import com.luxdone.file.service.model.FileData;
import com.luxdone.file.service.model.FileMetadata;
import java.util.List;
import java.util.Optional;

public interface FileRepository {

  String saveFile(FileData fileData);

  void updateFile(FileData newFileData);

  void removeFile(String fileId);

  void removeAll();

  Optional<FileData> getFile(String fileId);

  Optional<FileMetadata> getFileMetadata(String fileId);

  List<FileMetadata> listFileMetadata();


}
