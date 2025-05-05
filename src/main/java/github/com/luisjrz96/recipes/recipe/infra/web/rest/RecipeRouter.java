package github.com.luisjrz96.recipes.recipe.infra.web.rest;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@SuppressWarnings("unused")
public class RecipeRouter {

  @Bean
  public RouterFunction<ServerResponse> recipeRoutes(RecipeHandler handler) {
    return RouterFunctions.route(GET("/recipes"), handler::list)
        .andRoute(POST("/recipes"), handler::create)
        .andRoute(GET("/recipes/{id}"), handler::getById)
        .andRoute(DELETE("/recipes/{id}"), handler::delete)
        .andRoute(PUT("/recipes/{id}/publish"), handler::makePublic)
        .andRoute(GET("/recipes/user/{id}"), handler::listRecipesByCreator);
  }
}
