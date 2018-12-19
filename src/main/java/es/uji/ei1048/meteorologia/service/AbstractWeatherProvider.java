package es.uji.ei1048.meteorologia.service;

import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.model.converter.CityStringConverter;
import javafx.util.StringConverter;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public abstract class AbstractWeatherProvider implements IWeatherProvider {

    protected static final long SUGGESTION_COUNT = 7L;
    private static final @NotNull JaroWinklerDistance WINKLER_DISTANCE = new JaroWinklerDistance();

    /**
     * @param query The city query in lower case
     * @return The comparator that compares cities by converting them using the {@link CityStringConverter#toString(City)}
     * @implNote If the {@code query} is not in lower case the result may be unpredictable
     */
    protected static Comparator<City> getCityQueryComparator(@NonNls final @NotNull String query) {
        final @NotNull StringConverter<@NotNull City> converter = CityStringConverter.getInstance();
        return Comparator.comparingDouble(it -> -WINKLER_DISTANCE.apply(converter.toString(it).toLowerCase(Locale.ENGLISH), query));
    }

    @Override
    @SuppressWarnings("DesignForExtension")
    public @NotNull List<@NotNull WeatherData> getForecast(final @NotNull City city, final int offset) {
        return getForecast(city, offset, 1);
    }
}
