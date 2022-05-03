package com.luxdone.file.mongo.model;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@ToString
@Document(collection = FileMetadataMongo.COLLECTION)
public class FileMetadataMongo {

  public static final String COLLECTION = "metadata";

  @Id
  private String id;
  private String originalName;
  private String extension;
  private String mediaType;
  private Long size;

  private String fileId;

}
