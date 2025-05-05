package github.com.luisjrz96.recipes.recipe.domain.repository;

import github.com.luisjrz96.recipes.recipe.domain.entity.Recipe;
import github.com.luisjrz96.recipes.shared.infra.web.commons.PageResult;
import github.com.luisjrz96.recipes.shared.infra.web.commons.Pagination;
import reactor.core.publisher.Mono;

public interface RecipeRepository {
  Mono<Recipe> save(Recipe recipe);

  Mono<Recipe> findById(String id);

  Mono<Void> deleteById(String id);

  Mono<PageResult<Recipe>> findAllByIsPublishedTrue(Pagination pagination);

  Mono<PageResult<Recipe>> findRecipesByCreator(String userId, Pagination pagination);
}
