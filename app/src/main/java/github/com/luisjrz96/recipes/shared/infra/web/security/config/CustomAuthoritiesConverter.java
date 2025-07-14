package github.com.luisjrz96.recipes.shared.infra.web.security.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Flux;

public class CustomAuthoritiesConverter implements Converter<Jwt, Flux<GrantedAuthority>> {

  @Override
  public Flux<GrantedAuthority> convert(Jwt jwt) {
    List<String> roles = new ArrayList<>();

    if (jwt.getClaim("roles") != null) {
      roles = jwt.getClaimAsStringList("roles");
    } else if (jwt.getClaim("realm_access") != null) {
      var realmAccess = jwt.getClaimAsMap("realm_access");
      Object rolesObject = realmAccess.get("roles");
      if (rolesObject instanceof List<?>) {
        roles =
            ((List<?>) rolesObject)
                .stream().filter(String.class::isInstance).map(String.class::cast).toList();
      }
    }

    return Flux.fromIterable(
        roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList()));
  }
}
