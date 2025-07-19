package github.com.luisjrz96.recipes.category.infra.db.mongo;

import github.com.luisjrz96.recipes.category.domain.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

  public CategoryDocument toDocument(Category category) {
    return new CategoryDocument(category.getId(), category.getName());
  }

  public Category toEntity(CategoryDocument categoryDocument) {
    return new Category(categoryDocument.getId(), categoryDocument.getName());
  }
}
