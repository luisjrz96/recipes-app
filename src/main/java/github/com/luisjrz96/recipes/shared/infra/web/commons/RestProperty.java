package github.com.luisjrz96.recipes.shared.infra.web.commons;

public enum RestProperty {
  PAGE("page"),
  SIZE("size"),
  ID("id"),
  USER_CONTEXT("userContext"),
  ZERO("0");

  private final String text;

  RestProperty(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
