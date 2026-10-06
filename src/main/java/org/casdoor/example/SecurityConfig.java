package org.casdoor.example;

import org.casbin.casdoor.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthService authService,
                                            OAuth2AuthorizedClientService authorizedClientService) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/error").permitAll()
                        .anyRequest().authenticated())
                // sign in with Casdoor, configured by casdoor-spring-boot-starter
                .oauth2Login(Customizer.withDefaults())
                .logout(logout -> logout
                        // also end the user's session in Casdoor, so that signing in again asks for the password
                        .addLogoutHandler((request, response, authentication) -> {
                            if (authentication instanceof OAuth2AuthenticationToken token) {
                                OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                                        token.getAuthorizedClientRegistrationId(), token.getName());
                                if (client != null) {
                                    try {
                                        authService.logoutCurrentSession(client.getAccessToken().getTokenValue());
                                    } catch (RuntimeException e) {
                                        // the Casdoor session may already have ended
                                    }
                                }
                            }
                        })
                        .logoutSuccessUrl("/"));
        return http.build();
    }
}
