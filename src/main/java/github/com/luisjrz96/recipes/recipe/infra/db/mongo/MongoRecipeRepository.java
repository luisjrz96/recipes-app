package github.com.luisjrz96.recipes.recipe.infra.db.mongo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MongoRecipeRepository extends ReactiveMongoRepository<RecipeDocument, String> {

  Flux<RecipeDocument> findAllByIsPublishedTrue(Pageable pageable);

  Flux<RecipeDocument> findAllByCreator_Id(String creatorId, Pageable pageable);
}
