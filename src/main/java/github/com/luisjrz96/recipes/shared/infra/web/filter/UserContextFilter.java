package github.com.luisjrz96.recipes.shared.infra.web.filter;

import github.com.luisjrz96.recipes.shared.application.dto.UserDTO;
import github.com.luisjrz96.recipes.shared.infra.web.commons.RestProperty;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@Component
public class UserContextFilter implements WebFilter {

  private final String NAME_CLAIM = "name";
  private final String EMAIL_CLAIM = "email";

  @Override
  public Mono<Void> filter(@NonNull ServerWebExchange exchange, WebFilterChain chain) {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .cast(JwtAuthenticationToken.class)
        .map(
            auth -> {
              Jwt jwt = auth.getToken();
              String userId = jwt.getSubject();
              String username = jwt.getClaim(NAME_CLAIM);
              String email = jwt.getClaim(EMAIL_CLAIM);
              exchange
                  .getAttributes()
                  .put(RestProperty.USER_CONTEXT.toString(), new UserDTO(userId, username, email));
              return exchange;
            })
        .defaultIfEmpty(exchange)
        .flatMap(chain::filter);
  }
}
