package github.com.luisjrz96.recipes.shared.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class User {

  private String id;
  private String username;
  private String email;
}
