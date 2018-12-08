package es.uji.ei1048.meteorologia.service;

import es.uji.ei1048.meteorologia.model.WeatherData;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractWeatherService implements IWeatherService {

    static final @NotNull JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();

    private static final int DEFAULT_SUGGESTION_COUNT = 7;

    @Override
    @SuppressWarnings("DesignForExtension")
    public @NotNull List<@NotNull String> getSuggestedCities(final @NotNull String query) {
        return getSuggestedCities(query, DEFAULT_SUGGESTION_COUNT);
    }

    @Override
    @SuppressWarnings("DesignForExtension")
    public @NotNull List<@NotNull WeatherData> getForecast(final int cityId, final int offset) {
        return getForecast(cityId, offset, 1);
    }
}
