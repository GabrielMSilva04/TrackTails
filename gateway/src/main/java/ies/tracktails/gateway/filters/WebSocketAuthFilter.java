package ies.tracktails.gateway.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class WebSocketAuthFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authToken = exchange.getRequest().getQueryParams().getFirst("auth");

        if (authToken != null) {
            HttpHeaders modifiedHeaders = new HttpHeaders();
            exchange.getRequest().getHeaders().forEach(modifiedHeaders::addAll);
            modifiedHeaders.add("Authorization", "Bearer " + authToken);

            ServerHttpRequestDecorator modifiedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public HttpHeaders getHeaders() {
                    return modifiedHeaders;
                }
            };

            ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();

            return chain.filter(modifiedExchange);
        }

        return chain.filter(exchange);
    }
}
