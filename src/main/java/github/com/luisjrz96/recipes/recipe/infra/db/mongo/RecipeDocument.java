package github.com.luisjrz96.recipes.recipe.infra.db.mongo;

import github.com.luisjrz96.recipes.shared.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "recipes")
public class RecipeDocument {

  @Id private String id;

  @NotBlank private String title;

  @Size(min = 1)
  private List<@NotBlank String> ingredients;

  private String description;

  @Indexed private boolean isPublished;

  private String imageUrl;

  private User creator;
}
