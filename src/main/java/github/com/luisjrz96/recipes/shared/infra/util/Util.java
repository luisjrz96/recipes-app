package github.com.luisjrz96.recipes.shared.infra.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Util {

  private final Validator validator;

  public static List<String> getErrorsFromArrayString(String message) {
    if (!(message.length() > 2)) {
      return List.of();
    }
    message = message.substring(1, message.length() - 1);
    return Arrays.stream(message.split(",")).map(String::trim).toList();
  }

  public <T> Mono<T> validate(T obj) {
    var errors = validator.validate(obj);
    if (!errors.isEmpty()) {
      List<String> messages = errors.stream().map(ConstraintViolation::getMessage).toList();
      return Mono.error(new ValidationException(String.valueOf(messages)));
    }
    return Mono.just(obj);
  }
}
