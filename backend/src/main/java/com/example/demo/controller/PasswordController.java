package com.example.demo.controller;

import com.example.demo.PasswordGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordController {

    @GetMapping("/api/password")
    public String generate(@RequestParam(defaultValue = "12") int length) {
        return PasswordGenerator.generateSuperPassword(length);
    }
}
