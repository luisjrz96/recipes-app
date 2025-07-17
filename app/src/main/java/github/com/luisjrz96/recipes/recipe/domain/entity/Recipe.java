package github.com.luisjrz96.recipes.recipe.domain.entity;

import github.com.luisjrz96.recipes.shared.domain.entity.User;
import github.com.luisjrz96.recipes.shared.domain.exceptions.ResourceAccessException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Recipe {
  private final String id;
  private final User creator;
  private final String title;
  private final List<String> ingredients;
  private final String description;
  private final boolean isPublished;
  private final String imageUrl;

  public Recipe copyWith(
      String title,
      List<String> ingredients,
      String description,
      boolean isPublished,
      String imageUrl) {
    return new Recipe(
        this.id,
        this.creator,
        title != null ? title : this.title,
        ingredients != null ? new ArrayList<>(ingredients) : new ArrayList<>(),
        description != null ? description : this.description,
        isPublished || this.isPublished,
        imageUrl != null ? imageUrl : this.imageUrl);
  }

  public boolean isOwner(String userId) {
    if (userId != null && this.creator != null && !userId.equals(this.creator.getId())) {
      throw new ResourceAccessException(userId, "Recipe", this.id);
    }
    return true;
  }
}
