package ies.tracktails.userservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ies.tracktails.userservice.components.JwtTokenProvider;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class JwkSetRestController {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwkSetRestController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/pub_key")
    public ResponseEntity<Map<String, Object>> getPublicKey() throws IOException {
        // Adiciona a chave em uma lista e retorna no formato esperado
        Map<String, Object> jwk = jwtTokenProvider.getJwk();
        Map<String, Object> response = Collections.singletonMap("keys", Collections.singletonList(jwk));
        return ResponseEntity.ok(response);
    }
}