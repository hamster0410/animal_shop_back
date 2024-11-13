package com.example.firstproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class firstController {

    @GetMapping("/")
    public String fisrtGetMapping(Model model){
        System.out.println("i touched");
        model.addAttribute("username", "sunwoo");
        return "hello";
    }
}
