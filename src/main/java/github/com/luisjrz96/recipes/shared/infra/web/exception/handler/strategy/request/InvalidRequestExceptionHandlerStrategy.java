package github.com.luisjrz96.recipes.shared.infra.web.exception.handler.strategy.request;

import static github.com.luisjrz96.recipes.shared.infra.util.Util.getErrorsFromArrayString;

import github.com.luisjrz96.recipes.shared.application.exceptions.ResourceAccessException;
import github.com.luisjrz96.recipes.shared.application.exceptions.ResourceModificationException;
import github.com.luisjrz96.recipes.shared.application.exceptions.ResourceNotFoundException;
import github.com.luisjrz96.recipes.shared.infra.web.exception.handler.ErrorResponse;
import github.com.luisjrz96.recipes.shared.infra.web.exception.handler.strategy.ExceptionHandlerStrategy;
import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@Component
public class InvalidRequestExceptionHandlerStrategy implements ExceptionHandlerStrategy {

  @Override
  public boolean supports(Throwable throwable) {
    return throwable instanceof IllegalArgumentException
        || throwable instanceof ResourceModificationException
        || throwable instanceof ResourceNotFoundException
        || throwable instanceof ResourceAccessException
        || throwable instanceof ServerWebInputException
        || throwable instanceof ValidationException;
  }

  @Override
  public Mono<ErrorResponse> handle(ServerWebExchange webExchange, Throwable throwable) {
    List<String> errors = new ArrayList<>();

    if (throwable instanceof ValidationException) {
      errors = getErrorsFromArrayString(throwable.getMessage());
    } else {
      errors.add(throwable.getMessage());
    }

    return Mono.just(
        new ErrorResponse(
            400,
            webExchange.getRequest().getURI().toString(),
            webExchange.getRequest().getMethod().toString(),
            "Invalid request information",
            errors));
  }
}
