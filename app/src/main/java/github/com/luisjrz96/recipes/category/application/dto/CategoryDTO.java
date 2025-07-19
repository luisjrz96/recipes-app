package github.com.luisjrz96.recipes.category.application.dto;

import github.com.luisjrz96.recipes.category.domain.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryDTO {

  @NotBlank(message = "attribute name is required")
  private String name;

  public Category toEntity() {
    return new Category(null, name);
  }
}
