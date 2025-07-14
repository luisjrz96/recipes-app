package github.com.luisjrz96.recipes.recipe.application.usecases.authenticated.user;

import github.com.luisjrz96.recipes.recipe.domain.entity.Recipe;
import reactor.core.publisher.Mono;

public interface CreateRecipeUseCase {

  Mono<Recipe> createRecipe(Recipe recipe);
}
