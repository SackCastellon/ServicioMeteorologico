package es.uji.ei1048.meteorologia.service;

import es.uji.ei1048.meteorologia.model.WeatherData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class OpenWeather implements IWeatherService {
    @Override
    public @NotNull WeatherData getWeather(final @NotNull String cityName) {
        throw new UnsupportedOperationException("Not implemented!");
    }

    @Override
    public @NotNull List<@NotNull WeatherData> getForecast(final @NotNull String cityName, final int days) {
        throw new UnsupportedOperationException("Not implemented!");
    }
}
