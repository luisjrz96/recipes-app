package github.com.luisjrz96.recipes.shared.application.exceptions;

public class ResourceModificationException extends RuntimeException {

  public ResourceModificationException(String userId, String resourceName, String resourceId) {
    super(
        String.format(
            "User with id %s is not allowed to modify %s with id %s",
            userId, resourceName, resourceId));
  }
}
