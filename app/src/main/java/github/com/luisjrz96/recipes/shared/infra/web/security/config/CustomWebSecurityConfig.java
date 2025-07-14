package github.com.luisjrz96.recipes.shared.infra.web.security.config;

import github.com.luisjrz96.recipes.shared.infra.web.filter.UserContextFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@RequiredArgsConstructor
public class CustomWebSecurityConfig {

  private final UserContextFilter userContextFilter;

  @Bean
  public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
    http.csrf(ServerHttpSecurity.CsrfSpec::disable)
        .cors(ServerHttpSecurity.CorsSpec::disable)
        .authorizeExchange(
            exchanges ->
                exchanges
                    .pathMatchers(HttpMethod.GET, "/recipes")
                    .permitAll()
                    .pathMatchers("/recipes/**")
                    .authenticated()
                    .pathMatchers("/favorites/**")
                    .authenticated()
                    .anyExchange()
                    .authenticated())
        .oauth2ResourceServer(
            server ->
                server.jwt(
                    jwtSpec -> jwtSpec.jwtAuthenticationConverter(jwtAuthenticationConverter())))
        .addFilterAfter(userContextFilter, SecurityWebFiltersOrder.AUTHENTICATION);

    return http.build();
  }

  @Bean
  public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
    ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(new CustomAuthoritiesConverter());
    return converter;
  }
}
