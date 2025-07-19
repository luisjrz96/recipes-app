package github.com.luisjrz96.recipes.category.infra.web.rest;

import github.com.luisjrz96.recipes.category.application.dto.CategoryDTO;
import github.com.luisjrz96.recipes.category.application.usecases.CategoryApplicationUseCaseContainer;
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
public class CategoryHandler {

  private final CategoryApplicationUseCaseContainer applicationUseCaseContainer;
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
        .listCategories(new Pagination(page, size))
        .flatMap(result -> ServerResponse.ok().bodyValue(result));
  }

  public Mono<ServerResponse> create(ServerRequest serverRequest) {
    return serverRequest
        .bodyToMono(CategoryDTO.class)
        .flatMap(util::validate)
        .flatMap(categoryDTO -> Mono.just(categoryDTO.toEntity()))
        .flatMap(applicationUseCaseContainer::createCategory)
        .flatMap(saved -> ServerResponse.status(HttpStatus.CREATED).bodyValue(saved));
  }

  public Mono<ServerResponse> getById(ServerRequest serverRequest) {
    String categoryId = serverRequest.pathVariable(RestProperty.ID.toString());

    return applicationUseCaseContainer
        .findCategoryById(categoryId)
        .flatMap(result -> ServerResponse.ok().bodyValue(result));
  }

  public Mono<ServerResponse> delete(ServerRequest serverRequest) {
    String categoryId = serverRequest.pathVariable(RestProperty.ID.toString());
    return applicationUseCaseContainer
        .deleteCategory(categoryId)
        .then(ServerResponse.noContent().build());
  }

  public Mono<ServerResponse> update(ServerRequest serverRequest) {
    String categoryId = serverRequest.pathVariable(RestProperty.ID.toString());

    return applicationUseCaseContainer
        .findCategoryById(categoryId)
        .flatMap(category -> serverRequest.bodyToMono(CategoryDTO.class).flatMap(util::validate))
        .flatMap(
            categoryDTO ->
                applicationUseCaseContainer.updateCategory(categoryDTO.toEntity(), categoryId))
        .flatMap(updated -> ServerResponse.ok().bodyValue(updated));
  }
}
