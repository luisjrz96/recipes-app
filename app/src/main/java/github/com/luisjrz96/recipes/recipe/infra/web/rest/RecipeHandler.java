package github.com.luisjrz96.recipes.recipe.infra.web.rest;

import github.com.luisjrz96.recipes.recipe.application.RecipeApplicationUseCaseContainer;
import github.com.luisjrz96.recipes.recipe.application.dto.RecipeDTO;
import github.com.luisjrz96.recipes.shared.application.dto.UserDTO;
import github.com.luisjrz96.recipes.shared.infra.exceptions.InvalidUserException;
import github.com.luisjrz96.recipes.shared.infra.util.Util;
import github.com.luisjrz96.recipes.shared.infra.web.commons.Pagination;
import github.com.luisjrz96.recipes.shared.infra.web.commons.RestProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RecipeHandler {

  private final RecipeApplicationUseCaseContainer applicationUseCaseContainer;
  private final Util util;

  public Mono<ServerResponse> list(ServerRequest serverRequest) {
    int page =
        Integer.parseInt(
            serverRequest
                .queryParam(RestProperty.PAGE.toString())
                .orElse(RestProperty.ZERO.toString()));
    int size =
        Integer.parseInt(
            serverRequest
                .queryParam(RestProperty.SIZE.toString())
                .orElse(RestProperty.ZERO.toString()));

    return applicationUseCaseContainer
        .listPublishedRecipes(new Pagination(page, size))
        .flatMap(result -> ServerResponse.ok().bodyValue(result));
  }

  public Mono<ServerResponse> getById(ServerRequest serverRequest) {
    String recipeId = serverRequest.pathVariable(RestProperty.ID.toString());

    return Mono.justOrEmpty(
            serverRequest
                .attribute(RestProperty.USER_CONTEXT.toString())
                .filter(UserDTO.class::isInstance)
                .map(UserDTO.class::cast))
        .filter(userDto -> userDto.getId() != null)
        .switchIfEmpty(Mono.error(new InvalidUserException()))
        .flatMap(user -> applicationUseCaseContainer.getRecipeById(user.toEntity(), recipeId))
        .flatMap(recipe -> ServerResponse.ok().bodyValue(recipe));
  }

  public Mono<ServerResponse> create(ServerRequest serverRequest) {
    return Mono.justOrEmpty(
            serverRequest
                .attribute(RestProperty.USER_CONTEXT.toString())
                .filter(UserDTO.class::isInstance)
                .map(UserDTO.class::cast))
        .filter(userDto -> userDto.getId() != null)
        .switchIfEmpty(Mono.error(new InvalidUserException()))
        .flatMap(
            userDto ->
                serverRequest
                    .bodyToMono(RecipeDTO.class)
                    .flatMap(util::validate)
                    .flatMap(recipeDto -> Mono.just(recipeDto.toEntity(userDto))))
        .flatMap(applicationUseCaseContainer::createRecipe)
        .flatMap(saved -> ServerResponse.status(HttpStatus.CREATED).bodyValue(saved));
  }

  public Mono<ServerResponse> delete(ServerRequest serverRequest) {
    String recipeId = serverRequest.pathVariable(RestProperty.ID.toString());

    return Mono.justOrEmpty(
            serverRequest
                .attribute(RestProperty.USER_CONTEXT.toString())
                .filter(UserDTO.class::isInstance)
                .map(UserDTO.class::cast))
        .filter(userDto -> userDto.getId() != null)
        .switchIfEmpty(Mono.error(new InvalidUserException()))
        .switchIfEmpty(Mono.error(new InvalidUserException()))
        .flatMap(user -> applicationUseCaseContainer.deleteRecipe(user.toEntity(), recipeId))
        .then(ServerResponse.noContent().build());
  }

  public Mono<ServerResponse> makePublic(ServerRequest serverRequest) {
    String recipeId = serverRequest.pathVariable(RestProperty.ID.toString());

    return Mono.justOrEmpty(
            serverRequest
                .attribute(RestProperty.USER_CONTEXT.toString())
                .filter(UserDTO.class::isInstance)
                .map(UserDTO.class::cast))
        .filter(userDto -> userDto.getId() != null)
        .switchIfEmpty(Mono.error(new InvalidUserException()))
        .filter(user -> user.getId() != null)
        .switchIfEmpty(Mono.error(new InvalidUserException()))
        .flatMap(user -> applicationUseCaseContainer.makeRecipePublic(user.toEntity(), recipeId))
        .flatMap(modified -> ServerResponse.accepted().bodyValue(modified));
  }

  public Mono<ServerResponse> listRecipesByCreator(ServerRequest serverRequest) {
    int page =
        Integer.parseInt(
            serverRequest
                .queryParam(RestProperty.PAGE.toString())
                .orElse(RestProperty.ZERO.toString()));
    int size =
        Integer.parseInt(
            serverRequest
                .queryParam(RestProperty.SIZE.toString())
                .orElse(RestProperty.ZERO.toString()));

    return Mono.justOrEmpty(
            serverRequest
                .attribute(RestProperty.USER_CONTEXT.toString())
                .filter(UserDTO.class::isInstance)
                .map(UserDTO.class::cast))
        .filter(userDto -> userDto.getId() != null)
        .switchIfEmpty(Mono.error(new InvalidUserException()))
        .filter(user -> user.getId() != null)
        .switchIfEmpty(Mono.error(new InvalidUserException()))
        .flatMap(
            user ->
                applicationUseCaseContainer.getRecipesByCreator(
                    user.toEntity(), new Pagination(page, size)))
        .flatMap(result -> ServerResponse.ok().bodyValue(result));
  }
}
