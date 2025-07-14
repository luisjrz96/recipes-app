package github.com.luisjrz96.recipes.shared.infra.web.exception.handler.strategy.server;

import github.com.luisjrz96.recipes.shared.infra.web.exception.handler.ErrorResponse;
import github.com.luisjrz96.recipes.shared.infra.web.exception.handler.strategy.ExceptionHandlerStrategy;
import java.util.List;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class FallbackExceptionHandlerStrategy implements ExceptionHandlerStrategy {
  @Override
  public boolean supports(Throwable throwable) {
    return false;
  }

  @Override
  public Mono<ErrorResponse> handle(ServerWebExchange webExchange, Throwable throwable) {
    return Mono.just(
        new ErrorResponse(
            500,
            webExchange.getRequest().getURI().toString(),
            webExchange.getRequest().getMethod().toString(),
            "Internal Server Error",
            List.of(throwable.getMessage())));
  }
}
