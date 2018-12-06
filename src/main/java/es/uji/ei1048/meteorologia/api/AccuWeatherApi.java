package es.uji.ei1048.meteorologia.api;

import es.uji.ei1048.meteorologia.model.WeatherData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AccuWeatherApi implements IWeatherApi {
    @Override
    public @NotNull WeatherData getWeather(@NotNull String cityName) {
        return null;
    }

    @Override
    public @NotNull List<@NotNull WeatherData> getForecast(@NotNull String cityName, int days) {
        return null;
    }
}
