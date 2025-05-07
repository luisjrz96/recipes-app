package github.com.luisjrz96.recipes.shared.application.dto;

import github.com.luisjrz96.recipes.shared.domain.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDTO {

  @NotBlank(message = "User must contain a valid id")
  private String id;

  private String username;
  private String email;

  public User toEntity() {
    return new User(id, username, email);
  }
}
