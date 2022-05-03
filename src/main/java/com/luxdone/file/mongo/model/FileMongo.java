package com.luxdone.file.mongo.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@ToString
@Document(collection = FileMongo.COLLECTION)
public class FileMongo {

  public static final String COLLECTION = "file";

  @Id
  private String id;
  private Binary data;
}
