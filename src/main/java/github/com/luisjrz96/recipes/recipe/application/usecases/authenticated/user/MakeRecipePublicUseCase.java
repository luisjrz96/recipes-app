package github.com.luisjrz96.recipes.recipe.application.usecases.authenticated.user;

import github.com.luisjrz96.recipes.recipe.domain.entity.Recipe;
import github.com.luisjrz96.recipes.shared.domain.User;
import reactor.core.publisher.Mono;

public interface MakeRecipePublicUseCase {

  Mono<Recipe> makeRecipePublic(User user, String id);
}
