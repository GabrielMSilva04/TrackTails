package ies.tracktails.gateway.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfiguration {

    // Rota para permitir acesso sem autenticação para o actuator
    @Order(1)
    @Bean
    SecurityWebFilterChain actuatorHttpSecurity(ServerHttpSecurity http) {
        http
                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/actuator/**"))
                .authorizeExchange((exchanges) -> exchanges
                        .anyExchange().permitAll());
        return http.build();
    }

    // Configuração para autenticação JWT nas rotas que exigem
    @Bean
    SecurityWebFilterChain apiHttpSecurity(ServerHttpSecurity http) {
        http
                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/**"))  // Aplica apenas às rotas "/api/**"
                .authorizeExchange((exchanges) -> exchanges
                        .anyExchange().authenticated())  // Exige autenticação JWT
                .oauth2ResourceServer(OAuth2ResourceServerSpec::jwt)  // Configura o servidor de recursos OAuth2 com JWT
                .csrf().disable();  // Desabilita CSRF, se necessário
        return http.build();
    }

    // Rota para permitir acesso sem autenticação por padrão
    @Bean
    SecurityWebFilterChain defaultHttpSecurity(ServerHttpSecurity http) {
        http
                .authorizeExchange((exchanges) -> exchanges
                        .anyExchange().permitAll())  // Permite todas as requisições sem autenticação
                .csrf().disable();  // Desabilita CSRF, se necessário
        return http.build();
    }
}
