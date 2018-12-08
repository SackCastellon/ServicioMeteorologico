package es.uji.ei1048.meteorologia.api;

import org.jetbrains.annotations.NotNull;

public final class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(final @NotNull String message) {
        super(message);
    }
}
