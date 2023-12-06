package com.example.portal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.portal.models.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User getUserById(String username);

}
