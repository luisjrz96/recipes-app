package github.com.luisjrz96.recipes.shared.infra.web.security.config;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Flux;

public class CustomAuthoritiesConverter implements Converter<Jwt, Flux<GrantedAuthority>> {

  @SuppressWarnings("unchecked")
  @Override
  public Flux<GrantedAuthority> convert(Jwt jwt) {
    List<String> roles;

    if (jwt.getClaim("roles") != null) {
      roles = jwt.getClaimAsStringList("roles");
    } else if (jwt.getClaim("realm_access") != null) {
      var realmAccess = jwt.getClaimAsMap("realm_access");
      roles = (List<String>) realmAccess.get("roles");
    } else {
      roles = Collections.emptyList();
    }

    return Flux.fromIterable(
        roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList()));
  }
}
