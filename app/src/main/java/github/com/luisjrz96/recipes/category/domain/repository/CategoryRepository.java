package github.com.luisjrz96.recipes.category.domain.repository;

import github.com.luisjrz96.recipes.category.domain.entity.Category;
import github.com.luisjrz96.recipes.shared.infra.web.commons.PageResult;
import github.com.luisjrz96.recipes.shared.infra.web.commons.Pagination;
import reactor.core.publisher.Mono;

public interface CategoryRepository {

  Mono<Category> save(Category category);

  Mono<Category> findById(String id);

  Mono<Void> deleteById(String id);

  Mono<PageResult<Category>> findAll(Pagination pagination);
}
