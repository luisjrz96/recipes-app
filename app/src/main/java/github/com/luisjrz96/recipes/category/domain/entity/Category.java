package github.com.luisjrz96.recipes.category.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Category {
  private final String id;
  private final String name;

  public Category copyWith(String name) {
    return new Category(this.id, name != null && !name.isEmpty() ? name : this.name);
  }
}
