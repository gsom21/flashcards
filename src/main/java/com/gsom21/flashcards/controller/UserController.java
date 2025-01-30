package com.gsom21.flashcards.controller;

import com.gsom21.flashcards.domain.User;
import com.gsom21.flashcards.exception.StatusException;
import com.gsom21.flashcards.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@CrossOrigin(origins = "*")
@Controller
public class UserController {
    private final static String USER_FIELD = "u";
    private final static String PASSWORD_FIELD = "p";

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/user/login")
    public @ResponseBody User login(@RequestBody Map<String, Object> body) throws RuntimeException {
        if (body == null || body.isEmpty()) {
            throw new StatusException(HttpStatus.BAD_REQUEST, "invalid request");
        }

        if (!body.containsKey(USER_FIELD) || !body.containsKey(PASSWORD_FIELD)) {
            throw new StatusException(HttpStatus.BAD_REQUEST, "invalid request");
        }

        String username = body.get(USER_FIELD).toString();
        String password = body.get(PASSWORD_FIELD).toString();

        if (username.isEmpty() || password.isEmpty()) {
            throw new StatusException(HttpStatus.BAD_REQUEST, "invalid credentials");
        }

        User user = repository.findByUsername(username);
        if (user != null) {
            if (encoder.matches(password, user.getPassword())) {
                return user;
            }

            throw new StatusException(HttpStatus.BAD_REQUEST, "invalid credentials");
        }

        Collection<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        user = new User(username, this.encoder.encode(password), roles);
        repository.save(user);

        return user;
    }

    @GetMapping("/user/me")
    public @ResponseBody User me() throws RuntimeException {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof User) {
            return (User) user;
        }

        return null;
    }
}