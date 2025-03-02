package com.deepak.proexpenditure.pro_expenditure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Controller
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "redirect:/index"; // Redirects to static HTML file
    }
}