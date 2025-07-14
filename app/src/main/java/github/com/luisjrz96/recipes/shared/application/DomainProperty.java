package github.com.luisjrz96.recipes.shared.application;

public enum DomainProperty {
  RECIPE("Recipe");

  private final String text;

  DomainProperty(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
