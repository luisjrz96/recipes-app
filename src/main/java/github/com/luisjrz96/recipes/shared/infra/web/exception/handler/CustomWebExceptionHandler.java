package github.com.luisjrz96.recipes.shared.infra.web.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.com.luisjrz96.recipes.shared.infra.web.exception.handler.strategy.ExceptionHandlerStrategy;
import github.com.luisjrz96.recipes.shared.infra.web.exception.handler.strategy.server.FallbackExceptionHandlerStrategy;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class CustomWebExceptionHandler implements WebExceptionHandler {

  private final List<ExceptionHandlerStrategy> strategies;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull Throwable ex) {

    ExceptionHandlerStrategy strategy =
        strategies.stream()
            .filter(s -> s.supports(ex))
            .findFirst()
            .orElse(new FallbackExceptionHandlerStrategy());

    return strategy
        .handle(exchange, ex)
        .flatMap(
            response -> {
              exchange.getResponse().setStatusCode(HttpStatus.valueOf(response.status()));
              exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
              try {
                byte[] json = objectMapper.writeValueAsBytes(response);
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(json);
                return exchange.getResponse().writeWith(Mono.just(buffer));
              } catch (Exception e) {
                return Mono.error(e);
              }
            });
  }
}
