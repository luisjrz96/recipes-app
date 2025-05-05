package github.com.luisjrz96.recipes.recipe.domain.entity;

import github.com.luisjrz96.recipes.shared.domain.User;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Recipe {
  private final String id;
  private final String title;
  private final List<String> ingredients;
  private final String description;
  private boolean isPublished;
  private final String imageUrl;
  private final User creator;

  public void publish() {
    this.isPublished = true;
  }

  public void isPublished(boolean isPublished) {
    this.isPublished = isPublished;
  }
}
