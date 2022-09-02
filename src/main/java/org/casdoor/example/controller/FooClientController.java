package org.casdoor.example.controller;

import org.casdoor.example.model.FooModel;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FooClientController {
    @GetMapping("/foos")
    public String getFoos(Model model, Authentication authentication) {
        List<FooModel> foos = new ArrayList<>();
        foos.add(new FooModel(1L, "a"));
        foos.add(new FooModel(2L, "b"));
        foos.add(new FooModel(3L, "c"));
        foos.add(new FooModel(4L, authentication.getName()));
        model.addAttribute("foos", foos);
        return "foos";
    }
}