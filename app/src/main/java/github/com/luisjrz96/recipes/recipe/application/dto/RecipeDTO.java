package github.com.luisjrz96.recipes.recipe.application.dto;

import github.com.luisjrz96.recipes.recipe.domain.entity.Recipe;
import github.com.luisjrz96.recipes.shared.application.dto.UserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecipeDTO {

  @NotBlank(message = "Title is required")
  private String title;

  @Size(min = 1, message = "Recipe must contain at least 1 ingredient")
  List<@NotBlank(message = "Ingredient could not be empty") String> ingredients;

  @NotBlank(message = "Recipe must contain a valid description")
  private String description;

  @NotBlank(message = "Recipe must contain an imageUrl")
  private String imageUrl;

  private boolean isPublished;

  public Recipe toEntity(UserDTO creator) {
    return new Recipe(
        null, creator.toEntity(), title, ingredients, description, isPublished, imageUrl);
  }
}
