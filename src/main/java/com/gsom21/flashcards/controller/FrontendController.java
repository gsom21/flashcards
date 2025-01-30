package com.gsom21.flashcards.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    @GetMapping("/{path:[^\\.]*}")
    public String forwardToIndex() {
        return "forward:/index.html";
    }
}