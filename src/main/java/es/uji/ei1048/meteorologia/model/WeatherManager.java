package es.uji.ei1048.meteorologia.model;

import org.jetbrains.annotations.NotNull;

public enum WeatherManager {
    INSTANCE;

    public final boolean save(final @NotNull WeatherData data) {
        System.out.println("Guardado");
        return true;
    }

    public final @NotNull SaveFile load(final @NotNull String filename) {
        throw new UnsupportedOperationException("Not implemented!");
    }
}
