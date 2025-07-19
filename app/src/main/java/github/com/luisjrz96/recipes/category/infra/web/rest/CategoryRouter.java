package github.com.luisjrz96.recipes.category.infra.web.rest;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CategoryRouter {

  @Bean
  public RouterFunction<ServerResponse> categoryRoutes(CategoryHandler handler) {
    return RouterFunctions.route(GET("/categories"), handler::list)
        .andRoute(POST("/categories"), handler::create)
        .andRoute(GET("/categories/{id}"), handler::getById)
        .andRoute(DELETE("/categories/{id}"), handler::delete)
        .andRoute(PUT("/categories/{id}"), handler::update);
  }
}
