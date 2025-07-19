package github.com.luisjrz96.recipes.category.application.usecases.authenticated.admin;

import reactor.core.publisher.Mono;

public interface DeleteCategoryUseCase {
  Mono<Void> deleteCategory(String id);
}
