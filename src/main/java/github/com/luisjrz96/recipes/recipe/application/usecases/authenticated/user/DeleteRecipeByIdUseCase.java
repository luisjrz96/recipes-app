package github.com.luisjrz96.recipes.recipe.application.usecases.authenticated.user;

import github.com.luisjrz96.recipes.shared.domain.User;
import reactor.core.publisher.Mono;

public interface DeleteRecipeByIdUseCase {

  Mono<Void> deleteRecipe(User user, String id);
}
