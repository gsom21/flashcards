package com.gsom21.flashcards.repository;

import com.gsom21.flashcards.domain.Flashcard;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FlashcardRepository extends MongoRepository<Flashcard, String> {
    List<Flashcard> findByUserID(String userID);
    List<Flashcard> findByUserID(String userID, Sort sort);
    List<Flashcard> findByContent(String content);
}
