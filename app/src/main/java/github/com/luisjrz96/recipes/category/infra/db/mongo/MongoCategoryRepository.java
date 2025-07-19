package github.com.luisjrz96.recipes.category.infra.db.mongo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MongoCategoryRepository extends ReactiveMongoRepository<CategoryDocument, String> {
  Flux<CategoryDocument> findAllBy(Pageable pageable);
}
