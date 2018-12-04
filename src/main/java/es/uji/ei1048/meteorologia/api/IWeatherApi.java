package es.uji.ei1048.meteorologia.api;

import es.uji.ei1048.meteorologia.model.WeatherData;
import org.jetbrains.annotations.NotNull;

public interface IWeatherApi {
    @NotNull WeatherData getWeather(@NotNull String cityName) throws NotFoundException;

    @NotNull WeatherData getForecast(@NotNull String cityName, int days);
}
