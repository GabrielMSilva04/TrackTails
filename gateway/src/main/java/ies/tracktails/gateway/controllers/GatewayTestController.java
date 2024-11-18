package ies.tracktails.gateway.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayTestController {

    @GetMapping("/test/health")
    public String healthCheck() {
        return "API Gateway is working!";
    }
    
    @GetMapping("/api/health")
    public String healthApiCheck() {
        return "API Gateway is working!";
    }
}