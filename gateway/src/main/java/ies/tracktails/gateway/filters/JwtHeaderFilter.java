package ies.tracktails.gateway.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;

public class JwtHeaderFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        System.out.println("JwtHeaderFilter");

        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    System.out.println("SecurityContext: " + securityContext);

                    var authentication = securityContext.getAuthentication();
                    System.out.println("Authentication: " + authentication);

                    // Check if the authentication token is a JWT
                    if (authentication instanceof JwtAuthenticationToken) {
                        System.out.println("JwtAuthenticationToken");

                        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
                        String userId = jwt.getSubject();
                        System.out.println("UserId: " + userId);

                        // Create new HttpHeaders with the original headers + X-User-Id
                        HttpHeaders modifiedHeaders = new HttpHeaders();
                        exchange.getRequest().getHeaders().forEach(modifiedHeaders::addAll);
                        modifiedHeaders.add("X-User-Id", userId);

                        // Create a modified request with updated headers
                        ServerHttpRequestDecorator modifiedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                            @Override
                            public HttpHeaders getHeaders() {
                                return modifiedHeaders;
                            }
                        };

                        // Create a new exchange with the modified request
                        ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();

                        return chain.filter(modifiedExchange);
                    }

                    // If not a JWT, proceed with the original exchange
                    return chain.filter(exchange);
                })
                .switchIfEmpty(chain.filter(exchange));
    }
}
