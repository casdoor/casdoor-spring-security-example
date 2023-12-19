package org.casdoor.example.controller;

import org.casdoor.example.model.FooModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FooClientController {
    @GetMapping("/foos")
    public String getFoos(Model model, Authentication authentication,HttpSession session) {
        List<FooModel> foos = new ArrayList<>();
        foos.add(new FooModel(1L, "a"));
        foos.add(new FooModel(2L, "b"));
        foos.add(new FooModel(3L, "c"));
        foos.add(new FooModel(4L, authentication.getName()));
        model.addAttribute("foos", foos);
        DefaultOAuth2User user =(DefaultOAuth2User) authentication.getPrincipal();
        session.setAttribute("user",user);
        return "foos";
    }

    @Autowired
    RestTemplate  restTemplate;
    @GetMapping("/logout")
    public String myLogout(Model model,HttpSession session) {
        String forObject = restTemplate.getForObject("http://localhost:7001/api/logout", String.class);
        session.setAttribute("user",null);
        System.out.println(forObject);
        model.addAttribute("logoutSuccess", true);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "index";
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}