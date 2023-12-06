package com.example.portal.repository;

import com.example.portal.models.Classes;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClassesRepository extends MongoRepository<Classes,String> {
    Classes getClassesById(String id);
}
