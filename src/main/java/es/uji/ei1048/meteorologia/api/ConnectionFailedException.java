package es.uji.ei1048.meteorologia.api;

import org.jetbrains.annotations.NotNull;

/**
 * Indicates that the connection to the API service failed.
 */
public final class ConnectionFailedException extends RuntimeException {
    public ConnectionFailedException() {
    }

    public ConnectionFailedException(final @NotNull String message) {
        super(message);
    }
}
