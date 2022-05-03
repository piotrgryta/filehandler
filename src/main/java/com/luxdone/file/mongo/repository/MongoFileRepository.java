package com.luxdone.file.mongo.repository;

import com.luxdone.file.mongo.model.FileMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoFileRepository extends MongoRepository<FileMongo, String> {
}
