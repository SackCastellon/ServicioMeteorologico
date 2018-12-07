package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.service.IWeatherService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class SearchPane {
    private IWeatherService service;

    public void setService(final IWeatherService service) {
        this.service = service;
    }

    public @NotNull WeatherData getWeather(final @NotNull String query) {
        throw new UnsupportedOperationException("Not implemented!");
    }

    public @NotNull List<@NotNull WeatherData> getForecast(final @NotNull String query, final int days) {
        throw new UnsupportedOperationException("Not implemented!");
    }
}
