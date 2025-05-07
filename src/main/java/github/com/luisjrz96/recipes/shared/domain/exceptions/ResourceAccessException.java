package github.com.luisjrz96.recipes.shared.domain.exceptions;

public class ResourceAccessException extends RuntimeException {

  public ResourceAccessException(String userId, String resourceName, String resourceId) {
    super(
        String.format(
            "User with id %s is not allowed to access %s with id %s",
            userId, resourceName, resourceId));
  }
}
