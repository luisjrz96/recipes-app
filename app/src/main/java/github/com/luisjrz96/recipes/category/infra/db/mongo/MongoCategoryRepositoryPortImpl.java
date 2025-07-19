package github.com.luisjrz96.recipes.category.infra.db.mongo;

import github.com.luisjrz96.recipes.category.domain.entity.Category;
import github.com.luisjrz96.recipes.category.domain.repository.CategoryRepository;
import github.com.luisjrz96.recipes.shared.infra.web.commons.PageResult;
import github.com.luisjrz96.recipes.shared.infra.web.commons.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Primary
@RequiredArgsConstructor
public class MongoCategoryRepositoryPortImpl implements CategoryRepository {

  private final MongoCategoryRepository mongoCategoryRepository;
  private final CategoryMapper categoryMapper;

  @Override
  public Mono<Category> save(Category category) {
    return mongoCategoryRepository
        .save(categoryMapper.toDocument(category))
        .map(categoryMapper::toEntity);
  }

  @Override
  public Mono<Category> findById(String id) {
    return mongoCategoryRepository.findById(id).map(categoryMapper::toEntity);
  }

  @Override
  public Mono<Void> deleteById(String id) {
    return mongoCategoryRepository.deleteById(id);
  }

  @Override
  public Mono<PageResult<Category>> findAll(Pagination pagination) {
    Pageable pageable = PageRequest.of(pagination.page(), pagination.size());
    Flux<Category> categories =
        mongoCategoryRepository.findAllBy(pageable).map(categoryMapper::toEntity);
    Mono<Long> total = mongoCategoryRepository.count();

    return categories
        .collectList()
        .zipWith(total)
        .map(
            tuple ->
                new PageResult<>(
                    tuple.getT1(), pagination.page(), pagination.size(), tuple.getT2()));
  }
}
