package es.uji.ei1048.meteorologia.model;

import org.jetbrains.annotations.NotNull;

public class WeatherManager {
    private static final @NotNull WeatherManager INSTANCE = new WeatherManager();

    private WeatherManager() {
    }

    public static @NotNull WeatherManager getInstance() {
        return INSTANCE;
    }

    public final boolean save(final @NotNull WeatherData data) {
        throw new UnsupportedOperationException("Not implemented!");
    }

    public final @NotNull SaveFile load(final @NotNull String filename) {
        throw new UnsupportedOperationException("Not implemented!");
    }
}
