package ies.tracktails.gateway.filters;

import org.springframework.http.HttpHeaders;
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
                if (authentication instanceof JwtAuthenticationToken) {
                    System.out.println("JwtAuthenticationToken");
                    Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
                    String userId = jwt.getSubject();
                    System.out.println("UserId: " + userId);
                    exchange.getRequest().mutate()
                        .header("X-User-Id", userId)
                        .build();
                }
                return chain.filter(exchange);
            })
            .switchIfEmpty(chain.filter(exchange));
    }
}