package ies.tracktails.gateway.components;

import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwkTester {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @PostConstruct
    public void testJwkEndpoint() {
        try {
            String jwkResponse = WebClient.create()
                .get()
                .uri(jwkSetUri)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            System.out.println("JWK Response: " + jwkResponse);
        } catch (Exception e) {
            System.err.println("Failed to fetch JWK: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
