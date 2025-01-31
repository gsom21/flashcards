package com.gsom21.flashcards.controller;

import com.gsom21.flashcards.domain.Flashcard;
import com.gsom21.flashcards.domain.User;
import com.gsom21.flashcards.exception.StatusException;
import com.gsom21.flashcards.repository.FlashcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@Controller
public class FlashcardController {
    @Autowired
    private FlashcardRepository repository;

    @GetMapping("/flashcard/all")
    public @ResponseBody List<Flashcard> all() {
        Object userObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(userObject instanceof User user)) {
            throw new RuntimeException("User is not authenticated");
        }

        Sort sort = Sort.by(Sort.Order.desc("createdAt"));

        return repository.findByUserID(user.getId(), sort);
    }

    @PostMapping("/flashcard/delete")
    public @ResponseBody Flashcard delete(@RequestBody Map<String, Object> body) {
        final String ID_FIELD = "id";

        if (body == null || body.isEmpty()) {
            throw new StatusException(HttpStatus.BAD_REQUEST, "invalid request");
        }

        if (!body.containsKey(ID_FIELD)) {
            throw new StatusException(HttpStatus.BAD_REQUEST, "invalid request");
        }

        String id = body.get(ID_FIELD).toString();

        if (id == null || id.isEmpty()) {
            throw new StatusException(HttpStatus.BAD_REQUEST, "invalid request");
        }

        Object userObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(userObject instanceof User user)) {
            throw new RuntimeException("User is not authenticated");
        }

        Optional<Flashcard> flashcard = repository.findById(id);
        if (flashcard.isEmpty()) {
            throw new StatusException(HttpStatus.BAD_REQUEST, "flashcard not found");
        }

        if (!flashcard.get().getUserID().equals(user.getId())) {
            throw new StatusException(HttpStatus.BAD_REQUEST, "flashcard not found");
        }

        repository.delete(flashcard.get());

        return flashcard.get();
    }


    @PostMapping("/flashcard/import")
    public @ResponseBody void importCards(@RequestBody Map<String, Object> body) {
        final String TEXT_FIELD = "text";

        if (body == null || body.isEmpty()) {
            throw new StatusException(HttpStatus.BAD_REQUEST, "invalid request");
        }

        if (!body.containsKey(TEXT_FIELD)) {
            throw new StatusException(HttpStatus.BAD_REQUEST, "invalid request");
        }

        String text = body.get(TEXT_FIELD).toString();

        if (text == null || text.isEmpty()) {
            throw new StatusException(HttpStatus.BAD_REQUEST, "invalid request");
        }

        if (text.length() > 200000) {
            throw new StatusException(HttpStatus.BAD_REQUEST, "input is too big");
        }

        Object userObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(userObject instanceof User user)) {
            throw new RuntimeException("User is not authenticated");
        }

        String[] lines = text.split("\n");
        List<Flashcard> flashcards = new ArrayList<>();
        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            }
            if (line.length() > 300) {
                continue;
            }
            Flashcard flashcard = new Flashcard(line);
            flashcard.setUserID(user.getId());
            flashcard.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            flashcard.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            flashcards.add(flashcard);
        }

        repository.saveAll(flashcards);
    }

    @PostMapping("/flashcard/save")
    public @ResponseBody Flashcard save(@RequestBody Flashcard flashcard) {
        Object userObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(userObject instanceof User user)) {
            throw new RuntimeException("User is not authenticated");
        }

        String content = flashcard.getContent().trim();
        if (content.isEmpty()) {
            throw new StatusException(HttpStatus.BAD_REQUEST, "empty card");
        }

        if (content.length() > 300) {
            throw new StatusException(HttpStatus.BAD_REQUEST, "card is too big");
        }

        String hint = flashcard.getHint().trim();
        if (hint.length() > 300) {
            throw new StatusException(HttpStatus.BAD_REQUEST, "hint is too big");
        }

        if (flashcard.getId() != null && !flashcard.getId().isEmpty()) {
            Optional<Flashcard> flashcardOptional = repository.findById(flashcard.getId());
            if (flashcardOptional.isPresent()) {
                Flashcard existingFlashcard = flashcardOptional.get();
                if (!existingFlashcard.getUserID().equals(user.getId())) {
                    throw new StatusException(HttpStatus.BAD_REQUEST, "flashcard not found");
                }
                existingFlashcard.setContent(content);
                existingFlashcard.setHint(hint);
                existingFlashcard.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                repository.save(existingFlashcard);

                return existingFlashcard;
            } else {
                flashcard.setId("");
                flashcard.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            }
        } else {
            List<Flashcard> flashcards = repository.findByUserIDAndContent(user.getId(), content);
            if (!flashcards.isEmpty()) {
                throw new StatusException(HttpStatus.BAD_REQUEST, "flashcard already exists");
            }

            flashcard.setContent(content);
            flashcard.setHint(hint);
            flashcard.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        flashcard.setUserID(user.getId());
        flashcard.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        repository.save(flashcard);

        return flashcard;
    }
}
