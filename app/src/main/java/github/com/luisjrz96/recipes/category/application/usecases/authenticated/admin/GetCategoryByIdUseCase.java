package github.com.luisjrz96.recipes.category.application.usecases.authenticated.admin;

import github.com.luisjrz96.recipes.category.domain.entity.Category;
import reactor.core.publisher.Mono;

public interface GetCategoryByIdUseCase {
  Mono<Category> findCategoryById(String id);
}
