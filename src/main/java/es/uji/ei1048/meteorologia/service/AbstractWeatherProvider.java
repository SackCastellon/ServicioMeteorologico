package es.uji.ei1048.meteorologia.service;

import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.WeatherData;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractWeatherProvider implements IWeatherProvider {

    protected static final long SUGGESTION_COUNT = 7L;
    private static final @NotNull JaroWinklerDistance WINKLER_DISTANCE = new JaroWinklerDistance();

    protected static Comparator<City> getCityQueryComparator(@NonNls final @NotNull String query) {
        return Comparator.comparingDouble(it -> -WINKLER_DISTANCE.apply(query, it.getName().toLowerCase()));
    }

    @Override
    @SuppressWarnings("DesignForExtension")
    public @NotNull List<@NotNull WeatherData> getForecast(final @NotNull City city, final int offset) {
        return getForecast(city, offset, 1);
    }
}
