package ies.tracktails.gateway.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfiguration {
	// Configure JWT routes
	@Bean
	SecurityWebFilterChain apiHttpSecurity(ServerHttpSecurity http) {
		http
				.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/**"))
				.authorizeExchange((exchanges) -> exchanges
						.anyExchange().authenticated())
				.oauth2ResourceServer((oauth2) -> oauth2
				.jwt(Customizer.withDefaults()));

		return http.build();
	}

	// By default, permit all requests without authentication
	@Bean
	SecurityWebFilterChain defaultHttpSecurity(ServerHttpSecurity http) {
		http
				.authorizeExchange((exchanges) -> exchanges
						.anyExchange().permitAll())
				.csrf(csrf -> csrf.disable());
		return http.build();
	}
}
