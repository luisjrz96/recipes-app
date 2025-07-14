package github.com.luisjrz96.recipes.recipe.application.usecases.common;

import github.com.luisjrz96.recipes.recipe.domain.entity.Recipe;
import github.com.luisjrz96.recipes.shared.infra.web.commons.PageResult;
import github.com.luisjrz96.recipes.shared.infra.web.commons.Pagination;
import reactor.core.publisher.Mono;

public interface ListPublishedRecipePageUseCase {

  Mono<PageResult<Recipe>> listPublishedRecipes(Pagination pagination);
}
