package org.casdoor.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;

@SpringBootTest
@AutoConfigureMockMvc
class SSOClientApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void indexIsPublic() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    void foosNeedSigningInWithCasdoor() throws Exception {
        mockMvc.perform(get("/foos")).andExpect(redirectedUrl("http://localhost/oauth2/authorization/casdoor"));
        mockMvc.perform(get("/oauth2/authorization/casdoor"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", startsWith("https://door.casdoor.com/login/oauth/authorize?")));
    }

    @Test
    void foosShowTheSignedInUser() throws Exception {
        mockMvc.perform(get("/foos").with(oidcLogin().idToken(token -> token.claim("preferred_username", "alice"))))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("All Foos")));
    }
}
