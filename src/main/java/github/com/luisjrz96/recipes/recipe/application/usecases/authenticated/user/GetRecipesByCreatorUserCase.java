package github.com.luisjrz96.recipes.recipe.application.usecases.authenticated.user;

import github.com.luisjrz96.recipes.recipe.domain.entity.Recipe;
import github.com.luisjrz96.recipes.shared.domain.User;
import github.com.luisjrz96.recipes.shared.infra.web.commons.PageResult;
import github.com.luisjrz96.recipes.shared.infra.web.commons.Pagination;
import reactor.core.publisher.Mono;

public interface GetRecipesByCreatorUserCase {

  Mono<PageResult<Recipe>> getRecipesByCreator(User user, Pagination pagination);
}
