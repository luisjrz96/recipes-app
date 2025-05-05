package github.com.luisjrz96.recipes.recipe.infra.db.mongo;

import github.com.luisjrz96.recipes.recipe.domain.entity.Recipe;
import github.com.luisjrz96.recipes.recipe.domain.repository.RecipeRepository;
import github.com.luisjrz96.recipes.shared.infra.web.commons.PageResult;
import github.com.luisjrz96.recipes.shared.infra.web.commons.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@Repository
@Primary
@RequiredArgsConstructor
public class MongoRecipeRepositoryPortImpl implements RecipeRepository {

  private final MongoRecipeRepository mongoRecipeRepository;
  private final RecipeMapper recipeMapper;

  @Override
  public Mono<Recipe> save(Recipe recipe) {
    return mongoRecipeRepository.save(recipeMapper.toDocument(recipe)).map(recipeMapper::toEntity);
  }

  @Override
  public Mono<Recipe> findById(String id) {
    return mongoRecipeRepository.findById(id).map(recipeMapper::toEntity);
  }

  @Override
  public Mono<Void> deleteById(String id) {
    return mongoRecipeRepository.deleteById(id);
  }

  @Override
  public Mono<PageResult<Recipe>> findAllByIsPublishedTrue(Pagination pagination) {
    Pageable pageable = PageRequest.of(pagination.page(), pagination.size());
    Flux<Recipe> recipes =
        mongoRecipeRepository.findAllByIsPublishedTrue(pageable).map(recipeMapper::toEntity);
    Mono<Long> total = mongoRecipeRepository.count();

    return recipes
        .collectList()
        .zipWith(total)
        .map(
            tuple ->
                new PageResult<>(
                    tuple.getT1(), pagination.page(), pagination.size(), tuple.getT2()));
  }

  @Override
  public Mono<PageResult<Recipe>> findRecipesByCreator(String userId, Pagination pagination) {
    Pageable pageable = PageRequest.of(pagination.page(), pagination.size());

    Flux<Recipe> recipes =
        mongoRecipeRepository.findAllByCreator_Id(userId, pageable).map(recipeMapper::toEntity);
    Mono<Long> total = mongoRecipeRepository.count();
    return recipes
        .collectList()
        .zipWith(total)
        .map(
            tuple ->
                new PageResult<>(
                    tuple.getT1(), pagination.page(), pagination.size(), tuple.getT2()));
  }
}
