package ies.tracktails.gateway.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

import ies.tracktails.gateway.filters.JwtHeaderFilter;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfiguration {
	// CORS Configuration
	/*
	 * If necessary, you can configure CORS to allow requests from other domains.
	 * 
	 * @Bean
	 * public CorsConfigurationSource corsConfigurationSource() {
	 * CorsConfiguration configuration = new CorsConfiguration();
	 * // Permite que todas as origens acessem o gateway (use com cautela,
	 * dependendo da sua necessidade)
	 * configuration.addAllowedOrigin("*");
	 * configuration.addAllowedMethod("*"); // Permite todos os métodos HTTP (GET,
	 * POST, etc)
	 * configuration.addAllowedHeader("*"); // Permite todos os cabeçalhos
	 * 
	 * // Ou você pode restringir para um único frontend ou alguns domínios:
	 * // configuration.addAllowedOrigin("http://example.com");
	 * 
	 * UrlBasedCorsConfigurationSource source = new
	 * UrlBasedCorsConfigurationSource();
	 * source.registerCorsConfiguration("/**", configuration);
	 * return source;
	 * }
	 */

	 // Configure JWT routes
	@Bean
	@Order(1)
	SecurityWebFilterChain apiHttpSecurity(ServerHttpSecurity http) {
		http
		.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/v1/**"))
		.csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authorizeExchange(exchanges -> exchanges
            // Regras públicas
            .pathMatchers(
                "/api/v1/users/login",
				"/api/v1/users/register",
                "/api/v1/reports/**"
            ).permitAll()
            // Regras autenticadas
            .pathMatchers("/api/v1/**").authenticated()
            // Qualquer outra rota
            .anyExchange().permitAll())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

		// Adiciona o filtro de JWT (caso necessário)
		http.addFilterAfter(new JwtHeaderFilter(), SecurityWebFiltersOrder.AUTHENTICATION);														// add userId

		return http.build();
	}

	// By default, permit all requests without authentication
	@Bean
	@Order(2)
	SecurityWebFilterChain defaultHttpSecurity(ServerHttpSecurity http) {
		http
				.authorizeExchange((exchanges) -> exchanges
						.anyExchange().permitAll())
				.csrf(csrf -> csrf.disable());
		return http.build();
	}

	/*
	 * @Bean
	 * public ReactiveJwtDecoder jwtDecoder() {
	 * System.out.println("Initializing ReactiveJwtDecoder with JWK Set URI...");
	 * return NimbusReactiveJwtDecoder.withJwkSetUri(
	 * "http://userservice:8080/api/v1/pub_key").build();
	 * }
	 */
}
