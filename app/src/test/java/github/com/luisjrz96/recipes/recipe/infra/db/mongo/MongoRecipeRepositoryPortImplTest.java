package github.com.luisjrz96.recipes.recipe.infra.db.mongo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import github.com.luisjrz96.recipes.recipe.domain.entity.Recipe;
import github.com.luisjrz96.recipes.shared.domain.entity.User;
import github.com.luisjrz96.recipes.shared.infra.web.commons.PageResult;
import github.com.luisjrz96.recipes.shared.infra.web.commons.Pagination;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class MongoRecipeRepositoryPortImplTest {

  @InjectMocks private MongoRecipeRepositoryPortImpl mongoRecipeRepositoryPort;

  @Mock private RecipeMapper recipeMapper;

  @Mock private MongoRecipeRepository mongoRecipeRepository;

  @Test
  void testSave() {
    Recipe recipe = getRecipe(1);
    RecipeDocument recipeDocument = getRecipeDocumentFromRecipe(recipe);

    when(recipeMapper.toDocument(recipe)).thenReturn(recipeDocument);
    when(mongoRecipeRepository.save(recipeDocument)).thenReturn(Mono.just(recipeDocument));
    when(recipeMapper.toEntity(recipeDocument)).thenReturn(recipe);
    Mono<Recipe> result = mongoRecipeRepositoryPort.save(recipe);

    StepVerifier.create(result).expectNext(recipe).verifyComplete();
    verify(recipeMapper).toDocument(recipe);
    verify(mongoRecipeRepository).save(recipeDocument);
    verify(recipeMapper).toEntity(recipeDocument);
  }

  @Test
  void testFindById() {
    Recipe recipe = getRecipe(1);
    RecipeDocument recipeDocument = getRecipeDocumentFromRecipe(recipe);

    when(mongoRecipeRepository.findById(recipeDocument.getId()))
        .thenReturn(Mono.just(recipeDocument));
    when(recipeMapper.toEntity(recipeDocument)).thenReturn(recipe);
    Mono<Recipe> result = mongoRecipeRepositoryPort.findById(recipe.getId());

    StepVerifier.create(result).expectNext(recipe).verifyComplete();
    verify(mongoRecipeRepository).findById(recipeDocument.getId());
    verify(recipeMapper).toEntity(recipeDocument);
  }

  @Test
  void testDeleteById() {
    when(mongoRecipeRepository.deleteById("recipeId")).thenReturn(Mono.empty());
    mongoRecipeRepositoryPort.deleteById("recipeId");
    verify(mongoRecipeRepository).deleteById("recipeId");
  }

  @Test
  void testFindAllByIsPublishedTrue() {
    Pageable pageable = PageRequest.of(0, 10);
    List<Recipe> recipes = getRecipeList(10);
    List<RecipeDocument> recipeDocuments = getRecipeDocumentListFromRecipeList(recipes);

    when(mongoRecipeRepository.findAllByIsPublishedTrue(pageable))
        .thenReturn(Flux.fromIterable(recipeDocuments));
    when(mongoRecipeRepository.count()).thenReturn(Mono.just(10L));

    for (int i = 0; i < recipeDocuments.size(); i++) {
      when(recipeMapper.toEntity(recipeDocuments.get(i))).thenReturn(recipes.get(i));
    }

    Mono<PageResult<Recipe>> result =
        mongoRecipeRepositoryPort.findAllByIsPublishedTrue(new Pagination(0, 10));

    StepVerifier.create(result)
        .assertNext(
            pageResult -> {
              assertEquals(0, pageResult.page());
              assertEquals(10, pageResult.size());
              assertEquals(10L, pageResult.total());
              assertEquals(recipes, pageResult.content());
            })
        .verifyComplete();
    verify(mongoRecipeRepository).findAllByIsPublishedTrue(pageable);
    verify(mongoRecipeRepository).count();
  }

  @Test
  void testFindRecipesByCreator() {
    Pageable pageable = PageRequest.of(0, 10);
    List<Recipe> recipes = getRecipeList(10);
    List<RecipeDocument> recipeDocuments = getRecipeDocumentListFromRecipeList(recipes);

    when(mongoRecipeRepository.findAllByCreator_Id(
            recipeDocuments.getFirst().getCreator().getId(), pageable))
        .thenReturn(Flux.fromIterable(recipeDocuments));
    when(mongoRecipeRepository.count()).thenReturn(Mono.just(10L));

    for (int i = 0; i < recipeDocuments.size(); i++) {
      when(recipeMapper.toEntity(recipeDocuments.get(i))).thenReturn(recipes.get(i));
    }

    Mono<PageResult<Recipe>> result =
        mongoRecipeRepositoryPort.findRecipesByCreator(
            recipes.getFirst().getCreator().getId(), new Pagination(0, 10));

    StepVerifier.create(result)
        .assertNext(
            pageResult -> {
              assertEquals(0, pageResult.page());
              assertEquals(10, pageResult.size());
              assertEquals(10L, pageResult.total());
              assertEquals(recipes, pageResult.content());
            })
        .verifyComplete();
    verify(mongoRecipeRepository)
        .findAllByCreator_Id(recipes.getFirst().getCreator().getId(), pageable);
    verify(mongoRecipeRepository).count();
  }

  private Recipe getRecipe(int suffix) {
    User creator = new User("userId", "John Smith", "john.smith@chef.com");
    return new Recipe(
        String.format("%s%s", "recipeId", suffix),
        creator,
        String.format("%s%s", "title", suffix),
        List.of(String.format("%s%s", "ingredient", suffix)),
        String.format("%s%s", "description", suffix),
        false,
        String.format("%s%s", "imageUrl", suffix));
  }

  private RecipeDocument getRecipeDocumentFromRecipe(Recipe recipe) {
    return new RecipeDocument(
        recipe.getId(),
        recipe.getTitle(),
        recipe.getIngredients(),
        recipe.getDescription(),
        recipe.isPublished(),
        recipe.getImageUrl(),
        recipe.getCreator());
  }

  private List<Recipe> getRecipeList(int quantity) {
    List<Recipe> recipesList = new ArrayList<>();
    for (int i = 0; i < quantity; i++) {
      recipesList.add(getRecipe(i));
    }
    return recipesList;
  }

  private List<RecipeDocument> getRecipeDocumentListFromRecipeList(List<Recipe> recipes) {
    return recipes.stream().map(this::getRecipeDocumentFromRecipe).toList();
  }
}
