package ies.tracktails.gateway.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
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

	// Define open routes
	// /api/v1/animaldata/latest
	// /api/v1/animaldata/history
	@Bean
	@Order(1)
	SecurityWebFilterChain openHttpSecurity(ServerHttpSecurity http) {
		http
				.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/v1/**"))
				.authorizeExchange(exchanges -> exchanges
						.pathMatchers("/api/v1/animaldata/latest/**", "/api/v1/animaldata/historic/**").permitAll()
						.anyExchange().authenticated());
		return http.build();
	}

	// Configure JWT routes
	@Bean
	@Order(2)
	SecurityWebFilterChain apiHttpSecurity(ServerHttpSecurity http) {
		http
				.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/v1/**"))
				.authorizeExchange((exchanges) -> exchanges
						.anyExchange().authenticated())
				.oauth2ResourceServer((oauth2) -> oauth2
						.jwt(Customizer.withDefaults()));

		http.addFilterAt(new JwtHeaderFilter(), SecurityWebFiltersOrder.AUTHENTICATION); // Add filter to extract and add userId

		return http.build();
	}

	// By default, permit all requests without authentication
	@Bean
	@Order(3)
	SecurityWebFilterChain defaultHttpSecurity(ServerHttpSecurity http) {
		http
				.authorizeExchange((exchanges) -> exchanges
						.anyExchange().permitAll())
				.csrf(csrf -> csrf.disable());
		return http.build();
	}
}
