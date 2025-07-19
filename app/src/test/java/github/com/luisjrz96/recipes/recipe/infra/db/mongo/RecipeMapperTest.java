package github.com.luisjrz96.recipes.recipe.infra.db.mongo;

import static org.junit.jupiter.api.Assertions.*;

import github.com.luisjrz96.recipes.recipe.domain.entity.Recipe;
import github.com.luisjrz96.recipes.shared.domain.entity.User;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RecipeMapperTest {

  private static RecipeMapper recipeMapper;

  @BeforeAll
  static void setup() {
    recipeMapper = new RecipeMapper();
  }

  @Test
  void testToDocument() {
    Recipe recipe =
        new Recipe(
            "recipeId",
            new User("userId", "John Smith", "john.smith@chef.com"),
            "title",
            List.of("ingredient1", "ingredient2"),
            "description",
            false,
            "imageUrl");
    RecipeDocument recipeDocument = recipeMapper.toDocument(recipe);
    assertNotNull(recipeDocument);
    assertEquals("recipeId", recipeDocument.getId());
    assertEquals("title", recipeDocument.getTitle());
    assertEquals("userId", recipeDocument.getCreator().getId());
    assertFalse(recipeDocument.isPublished());
  }

  @Test
  void testToEntity() {
    RecipeDocument recipeDocument = new RecipeDocument();
    recipeDocument.setId("recipeId");
    recipeDocument.setPublished(true);
    recipeDocument.setCreator(new User("userId", "John Smith", "john.smit@chef.com"));
    recipeDocument.setImageUrl("imageUrl");
    recipeDocument.setTitle("title");
    recipeDocument.setDescription("description");
    recipeDocument.setIngredients(List.of("ingredient1", "ingredient2"));
    Recipe recipe = recipeMapper.toEntity(recipeDocument);
    assertEquals("recipeId", recipe.getId());
    assertEquals("title", recipe.getTitle());
    assertEquals("userId", recipeDocument.getCreator().getId());
    assertTrue(recipeDocument.isPublished());
  }
}
