package github.com.luisjrz96.recipes.shared.infra.web.exception.handler.strategy;

import github.com.luisjrz96.recipes.shared.infra.web.exception.handler.ErrorResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface ExceptionHandlerStrategy {

  boolean supports(Throwable throwable);

  Mono<ErrorResponse> handle(ServerWebExchange webExchange, Throwable throwable);
}
