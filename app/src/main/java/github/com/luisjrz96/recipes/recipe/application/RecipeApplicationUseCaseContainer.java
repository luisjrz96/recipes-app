package github.com.luisjrz96.recipes.recipe.application;

import github.com.luisjrz96.recipes.recipe.application.usecases.authenticated.user.*;
import github.com.luisjrz96.recipes.recipe.application.usecases.common.ListPublishedRecipePageUseCase;
import github.com.luisjrz96.recipes.recipe.domain.entity.Recipe;
import github.com.luisjrz96.recipes.recipe.domain.repository.RecipeRepository;
import github.com.luisjrz96.recipes.shared.application.DomainProperty;
import github.com.luisjrz96.recipes.shared.application.exceptions.ResourceNotFoundException;
import github.com.luisjrz96.recipes.shared.domain.entity.User;
import github.com.luisjrz96.recipes.shared.infra.web.commons.PageResult;
import github.com.luisjrz96.recipes.shared.infra.web.commons.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class RecipeApplicationUseCaseContainer
    implements CreateRecipeUseCase,
        DeleteRecipeByIdUseCase,
        GetRecipeByIdUseCase,
        ListPublishedRecipePageUseCase,
        MakeRecipePublicUseCase,
        GetRecipesByCreatorUserCase {

  private final RecipeRepository recipeRepository;

  public Mono<Recipe> createRecipe(Recipe recipe) {
    return recipeRepository.save(recipe);
  }

  public Mono<Void> deleteRecipe(User user, String id) {
    return recipeRepository
        .findById(id)
        .switchIfEmpty(
            Mono.error(new ResourceNotFoundException(DomainProperty.RECIPE.toString(), id)))
        .filter(recipe -> recipe.isOwner(user.getId()))
        .flatMap(recipe -> recipeRepository.deleteById(id));
  }

  public Mono<Recipe> getRecipeById(User user, String id) {
    return recipeRepository
        .findById(id)
        .switchIfEmpty(
            Mono.error(new ResourceNotFoundException(DomainProperty.RECIPE.toString(), id)))
        .filter(recipe -> recipe.isOwner(user.getId()));
  }

  public Mono<PageResult<Recipe>> listPublishedRecipes(Pagination pagination) {
    return recipeRepository.findAllByIsPublishedTrue(pagination);
  }

  @Override
  public Mono<Recipe> makeRecipePublic(User user, String id) {
    return recipeRepository
        .findById(id)
        .switchIfEmpty(
            Mono.error(new ResourceNotFoundException(DomainProperty.RECIPE.toString(), id)))
        .filter(recipe -> recipe.isOwner(user.getId()))
        .flatMap(recipe -> Mono.just(recipe.copyWith(null, null, null, true, null)))
        .flatMap(recipeRepository::save);
  }

  @Override
  public Mono<PageResult<Recipe>> getRecipesByCreator(User user, Pagination pagination) {
    return recipeRepository.findRecipesByCreator(user.getId(), pagination);
  }
}
