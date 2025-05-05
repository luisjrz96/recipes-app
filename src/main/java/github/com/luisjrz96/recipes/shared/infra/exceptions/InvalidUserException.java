package github.com.luisjrz96.recipes.shared.infra.exceptions;

public class InvalidUserException extends RuntimeException {

  public InvalidUserException() {
    super("Invalid user in authentication");
  }
}
