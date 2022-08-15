package com.westerhoud.osrs.taskman.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    String index() {
        return "index";
    }

    @GetMapping("/admin")
    String admin() {
        return "admin";
    }

    @GetMapping("/user")
    String user() {
        return "user";
    }
}
