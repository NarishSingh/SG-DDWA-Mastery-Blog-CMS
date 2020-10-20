package com.sg.blogcms.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Load the home page
     *
     * @return {String} main domain
     */
    @GetMapping({"/", "/home"})
    public String displayHomePage() {
        return "home";
    }
}
