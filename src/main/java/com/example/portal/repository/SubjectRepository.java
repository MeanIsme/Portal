package com.example.portal.repository;

import com.example.portal.models.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubjectRepository extends MongoRepository<Subject, String> {
}
