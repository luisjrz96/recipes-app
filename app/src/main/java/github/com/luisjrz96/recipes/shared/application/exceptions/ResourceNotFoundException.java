package github.com.luisjrz96.recipes.shared.application.exceptions;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String entityName, String id) {
    super(String.format("%s with id %s not found", entityName, id));
  }
}
