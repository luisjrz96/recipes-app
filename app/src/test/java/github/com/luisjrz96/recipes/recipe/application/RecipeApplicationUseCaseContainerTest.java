package github.com.luisjrz96.recipes.recipe.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import github.com.luisjrz96.recipes.recipe.domain.entity.Recipe;
import github.com.luisjrz96.recipes.recipe.domain.repository.RecipeRepository;
import github.com.luisjrz96.recipes.shared.application.exceptions.ResourceNotFoundException;
import github.com.luisjrz96.recipes.shared.domain.entity.User;
import github.com.luisjrz96.recipes.shared.domain.exceptions.ResourceAccessException;
import github.com.luisjrz96.recipes.shared.infra.web.commons.PageResult;
import github.com.luisjrz96.recipes.shared.infra.web.commons.Pagination;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class RecipeApplicationUseCaseContainerTest {

  @InjectMocks private RecipeApplicationUseCaseContainer recipeApplicationUseCaseContainer;

  @Mock private RecipeRepository recipeRepository;

  @Test
  void testCreateRecipe_Successfully() {
    Recipe recipeInput = generateRecipe(null);
    Recipe recipeOutput = generateRecipe("recipeId");
    when(recipeRepository.save(recipeInput)).thenReturn(Mono.just(recipeOutput));

    Mono<Recipe> monoResult = recipeApplicationUseCaseContainer.createRecipe(recipeInput);
    Recipe result = monoResult.block();

    assertNotNull(result, "Recipe should not be null");
    assertEquals("recipeId", result.getId(), "Recipe ID should match expected value");
  }

  @Test
  void testDeleteRecipe_Successfully() {
    Recipe recipeInput = generateRecipe("recipeId");
    User user = new User("userId", "jhon smith", "jhon.smith@chef.com");

    when(recipeRepository.deleteById("recipeId")).thenReturn(Mono.empty());
    when(recipeRepository.findById("recipeId")).thenReturn(Mono.just(recipeInput));
    recipeApplicationUseCaseContainer.deleteRecipe(user, "recipeId").block();

    verify(recipeRepository, times(1)).deleteById("recipeId");
  }

  @Test
  void testDeleteRecipe_Fail_ResourceNotFoundException() {
    User user = new User("userId", "jhon smith", "jhon.smith@chef.com");
    when(recipeRepository.findById("recipeId")).thenReturn(Mono.empty());
    assertThrows(
        ResourceNotFoundException.class,
        () -> recipeApplicationUseCaseContainer.deleteRecipe(user, "recipeId").block());
  }

  @Test
  void testDeleteRecipe_FailInvalidUser_ResourceModificationException() {
    Recipe recipeInput = generateRecipe("recipeId");
    User resourceOwner = new User("ownerId", "Jane Doe", "jane.doe@chef.com");

    when(recipeRepository.findById("recipeId")).thenReturn(Mono.just(recipeInput));
    assertThrows(
        ResourceAccessException.class,
        () -> recipeApplicationUseCaseContainer.deleteRecipe(resourceOwner, "recipeId").block());
  }

  @Test
  void testGetRecipeById_Successfully() {
    Recipe recipeInput = generateRecipe("recipeId");
    User user = new User("userId", "jhon smith", "jhon.smith@chef.com");

    when(recipeRepository.findById("recipeId")).thenReturn(Mono.just(recipeInput));
    recipeApplicationUseCaseContainer.getRecipeById(user, "recipeId").block();

    verify(recipeRepository, times(1)).findById("recipeId");
  }

  @Test
  void testGetRecipeById_Fail_ResourceNotFoundException() {
    User user = new User("userId", "jhon smith", "jhon.smith@chef.com");

    when(recipeRepository.findById("recipeId")).thenReturn(Mono.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> recipeApplicationUseCaseContainer.getRecipeById(user, "recipeId").block());

    verify(recipeRepository, times(1)).findById("recipeId");
  }

  @Test
  void testGetRecipeById_FailInvalidUser_ResourceAccessException() {
    Recipe recipeInput = generateRecipe("recipeId");
    User resourceOwner = new User("ownerId", "Jane Doe", "jane.doe@chef.com");

    when(recipeRepository.findById("recipeId")).thenReturn(Mono.just(recipeInput));

    assertThrows(
        ResourceAccessException.class,
        () -> recipeApplicationUseCaseContainer.getRecipeById(resourceOwner, "recipeId").block());

    verify(recipeRepository, times(1)).findById("recipeId");
  }

  @Test
  void testListPublishedRecipes() {
    Pagination pagination = new Pagination(0, 10);
    Recipe recipe1 = generateRecipe("recipeId");
    when(recipeRepository.findAllByIsPublishedTrue(pagination))
        .thenReturn(Mono.just(new PageResult<>(List.of(recipe1), 0, 10, 1)));

    PageResult<Recipe> recipesPage =
        recipeApplicationUseCaseContainer.listPublishedRecipes(pagination).block();
    assertEquals(1, Objects.requireNonNull(recipesPage).total());
    assertEquals(0, recipesPage.page());
    assertEquals(10, recipesPage.size());
    assertEquals("recipeId", recipesPage.content().getFirst().getId());
  }

  @Test
  void testMakeRecipePublic_Successfully() {
    User user = new User("userId", "jhon smith", "jhon.smith@chef.com");
    Recipe recipeInput = generateRecipe("recipeId");
    when(recipeRepository.save(any(Recipe.class)))
        .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
    when(recipeRepository.findById("recipeId")).thenReturn(Mono.just(recipeInput));
    Recipe recipeResult =
        recipeApplicationUseCaseContainer.makeRecipePublic(user, "recipeId").block();

    assertTrue(Objects.requireNonNull(recipeResult).isPublished());
  }

  @Test
  void testMakeRecipePublic_Fail_ResourceNotFoundException() {
    User user = new User("userId", "jhon smith", "jhon.smith@chef.com");
    when(recipeRepository.findById("recipeId")).thenReturn(Mono.empty());
    assertThrows(
        ResourceNotFoundException.class,
        () -> recipeApplicationUseCaseContainer.makeRecipePublic(user, "recipeId").block());
  }

  @Test
  void testMakeRecipePublic_Fail_ResourceModificationException() {
    User resourceOwner = new User("ownerId", "Jane Doe", "jane.doe@chef.com");
    Recipe recipeInput = generateRecipe("recipeId");

    when(recipeRepository.findById("recipeId")).thenReturn(Mono.just(recipeInput));
    assertThrows(
        ResourceAccessException.class,
        () ->
            recipeApplicationUseCaseContainer.makeRecipePublic(resourceOwner, "recipeId").block());
  }

  @Test
  void testGetRecipesByCreator() {
    User user = new User("userId", "jhon smith", "jhon.smith@chef.com");

    Pagination pagination = new Pagination(0, 10);
    Recipe recipe1 = generateRecipe("recipeId");
    when(recipeRepository.findRecipesByCreator(user.getId(), pagination))
        .thenReturn(Mono.just(new PageResult<>(List.of(recipe1), 0, 10, 1)));

    PageResult<Recipe> recipesPage =
        recipeApplicationUseCaseContainer.getRecipesByCreator(user, pagination).block();
    assertEquals(1, Objects.requireNonNull(recipesPage).total());
    assertEquals(0, recipesPage.page());
    assertEquals(10, recipesPage.size());
    assertEquals("recipeId", recipesPage.content().getFirst().getId());
  }

  private Recipe generateRecipe(String id) {
    return new Recipe(
        id,
        new User("userId", "jhon smith", "jhon.smith@chef.com"),
        "Tacos",
        List.of("Ingredient1", "Ingredient2", "Ingredient3"),
        "ABCDEFG",
        false,
        "/tacos.jpg");
  }
}
