package github.com.luisjrz96.recipes.shared.infra.web.exception.handler;

import java.util.List;

public record ErrorResponse(
    int status, String path, String method, String message, List<String> errors) {}
