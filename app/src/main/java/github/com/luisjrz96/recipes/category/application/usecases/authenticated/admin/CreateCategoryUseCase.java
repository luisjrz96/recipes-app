package github.com.luisjrz96.recipes.category.application.usecases.authenticated.admin;

import github.com.luisjrz96.recipes.category.domain.entity.Category;
import reactor.core.publisher.Mono;

public interface CreateCategoryUseCase {
  Mono<Category> createCategory(Category category);
}
