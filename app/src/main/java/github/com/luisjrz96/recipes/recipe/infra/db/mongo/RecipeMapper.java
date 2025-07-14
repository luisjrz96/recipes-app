package github.com.luisjrz96.recipes.recipe.infra.db.mongo;

import github.com.luisjrz96.recipes.recipe.domain.entity.Recipe;
import org.springframework.stereotype.Component;

@Component
public class RecipeMapper {

  public RecipeDocument toDocument(Recipe recipe) {
    RecipeDocument doc = new RecipeDocument();
    doc.setId(recipe.getId());
    doc.setTitle(recipe.getTitle());
    doc.setIngredients(recipe.getIngredients());
    doc.setDescription(recipe.getDescription());
    doc.setImageUrl(recipe.getImageUrl());
    doc.setCreator(recipe.getCreator());
    doc.setPublished(recipe.isPublished());
    return doc;
  }

  public Recipe toEntity(RecipeDocument doc) {
    return new Recipe(
        doc.getId(),
        doc.getCreator(),
        doc.getTitle(),
        doc.getIngredients(),
        doc.getDescription(),
        doc.isPublished(),
        doc.getImageUrl());
  }
}
