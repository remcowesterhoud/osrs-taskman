package com.westerhoud.osrs.taskman.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/")
public class MainController {

    @Value("${plugin.url}")
    private String pluginUrl;

    @GetMapping
    void redirectToPluginRepo(HttpServletResponse response) throws IOException {
        response.sendRedirect(pluginUrl);
    }
}
