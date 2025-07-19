package github.com.luisjrz96.recipes.category.application.usecases;

import github.com.luisjrz96.recipes.category.application.usecases.authenticated.admin.CreateCategoryUseCase;
import github.com.luisjrz96.recipes.category.application.usecases.authenticated.admin.DeleteCategoryUseCase;
import github.com.luisjrz96.recipes.category.application.usecases.authenticated.admin.GetCategoryByIdUseCase;
import github.com.luisjrz96.recipes.category.application.usecases.authenticated.admin.UpdateCategoryUseCase;
import github.com.luisjrz96.recipes.category.application.usecases.common.ListAllCategoriesUseCase;
import github.com.luisjrz96.recipes.category.domain.entity.Category;
import github.com.luisjrz96.recipes.category.domain.repository.CategoryRepository;
import github.com.luisjrz96.recipes.shared.application.DomainProperty;
import github.com.luisjrz96.recipes.shared.application.exceptions.ResourceNotFoundException;
import github.com.luisjrz96.recipes.shared.infra.web.commons.PageResult;
import github.com.luisjrz96.recipes.shared.infra.web.commons.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class CategoryApplicationUseCaseContainer
    implements ListAllCategoriesUseCase,
        CreateCategoryUseCase,
        DeleteCategoryUseCase,
        GetCategoryByIdUseCase,
        UpdateCategoryUseCase {

  private final CategoryRepository categoryRepository;

  @Override
  public Mono<Category> createCategory(Category category) {
    return categoryRepository.save(category);
  }

  @Override
  public Mono<Category> findCategoryById(String id) {
    return categoryRepository
        .findById(id)
        .switchIfEmpty(
            Mono.error(new ResourceNotFoundException(DomainProperty.CATEGORY.toString(), id)));
  }

  @Override
  public Mono<Category> updateCategory(Category newData, String id) {
    return categoryRepository
        .findById(id)
        .switchIfEmpty(
            Mono.error(new ResourceNotFoundException(DomainProperty.CATEGORY.toString(), id)))
        .map(category -> category.copyWith(newData.getName()))
        .flatMap(categoryRepository::save);
  }

  @Override
  public Mono<PageResult<Category>> listCategories(Pagination pagination) {
    return categoryRepository.findAll(pagination);
  }

  @Override
  public Mono<Void> deleteCategory(String id) {
    return categoryRepository.deleteById(id);
  }
}
