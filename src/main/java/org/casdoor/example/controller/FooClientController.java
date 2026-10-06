package org.casdoor.example.controller;

import org.casdoor.example.model.FooModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FooClientController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // needs a signed-in user, see SecurityConfig
    @GetMapping("/foos")
    public String getFoos(Model model, @AuthenticationPrincipal OidcUser user) {
        model.addAttribute("foos", List.of(
                new FooModel(1L, "a"),
                new FooModel(2L, "b"),
                new FooModel(3L, "c"),
                new FooModel(4L, user.getName())));
        model.addAttribute("user", user);
        return "foos";
    }
}
