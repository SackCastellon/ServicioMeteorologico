package es.uji.ei1048.meteorologia.api;

import org.jetbrains.annotations.NotNull;

/**
 * Indicates that an error occurred while parsing the response data.
 */
public final class DataParsingException extends RuntimeException {
    public DataParsingException(final @NotNull String message) {
        super(message);
    }
}
