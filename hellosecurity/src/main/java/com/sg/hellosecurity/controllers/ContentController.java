package com.sg.hellosecurity.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {

    @GetMapping("/content")
    public String displayContentPage() {
        return "content";
    }
}
