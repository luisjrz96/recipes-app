package github.com.luisjrz96.recipes.shared.infra.web.commons;

import java.util.List;

public record PageResult<T>(List<T> content, int page, int size, long total) {}
