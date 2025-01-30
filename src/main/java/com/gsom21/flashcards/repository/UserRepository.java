package com.gsom21.flashcards.repository;

import com.gsom21.flashcards.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}
