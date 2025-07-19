package github.com.luisjrz96.recipes.category.application.usecases.common;

import github.com.luisjrz96.recipes.category.domain.entity.Category;
import github.com.luisjrz96.recipes.shared.infra.web.commons.PageResult;
import github.com.luisjrz96.recipes.shared.infra.web.commons.Pagination;
import reactor.core.publisher.Mono;

public interface ListAllCategoriesUseCase {
  Mono<PageResult<Category>> listCategories(Pagination pagination);
}
