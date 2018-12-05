package es.uji.ei1048.meteorologia.api;

import org.jetbrains.annotations.NotNull;

/**
 * Indicates that the API service responded with a 404 status code
 */
public final class NotFoundException extends RuntimeException {
    public NotFoundException(final @NotNull String message) {
        super(message);
    }
}
