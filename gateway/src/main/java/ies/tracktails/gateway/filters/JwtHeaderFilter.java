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

        // Obtém o contexto de segurança reativo
        return ReactiveSecurityContextHolder.getContext()
            .flatMap(securityContext -> {
                System.out.println("SecurityContext: " + securityContext);
                var authentication = securityContext.getAuthentication();
                System.out.println("Authentication: " + authentication);

                // Se o token for do tipo JWT, extraímos o 'sub' (userId)
                if (authentication instanceof JwtAuthenticationToken) {
                    System.out.println("JwtAuthenticationToken");
                    Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
                    String userId = jwt.getSubject();
                    System.out.println("UserId: " + userId);

                    // Cria uma nova instância de HttpHeaders e adiciona o cabeçalho 'X-User-Id'
                    HttpHeaders modifiedHeaders = new HttpHeaders();
                    modifiedHeaders.putAll(exchange.getRequest().getHeaders());
                    modifiedHeaders.add("X-User-Id", userId); // Adiciona o cabeçalho

                    // Cria a requisição modificada com os cabeçalhos alterados
                    ServerHttpRequestDecorator modifiedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                        @Override
                        public HttpHeaders getHeaders() {
                            return modifiedHeaders;  // Retorna os cabeçalhos modificados
                        }
                    };

                    // Cria um novo ServerWebExchange com a requisição modificada
                    ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();

                    // Prossegue com a requisição modificada
                    return chain.filter(modifiedExchange);
                }

                // Caso o token não seja um JWT, passa a requisição original
                return chain.filter(exchange);
            });
    }
}
