package ies.tracktails.gateway.filters;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

public class JwtHeaderFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Retrieve the authentication object
        Authentication authentication = exchange.getAttribute(Authentication.class.getName());
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
            String userId = jwtAuth.getToken().getClaimAsString("userId");  // Extract userId from JWT claims

            // Add the userId to the request headers
            exchange.getRequest().mutate()
                .header("X-User-Id", userId)  // Add the userId in custom header
                .build();
        }
        return chain.filter(exchange);
    }
}