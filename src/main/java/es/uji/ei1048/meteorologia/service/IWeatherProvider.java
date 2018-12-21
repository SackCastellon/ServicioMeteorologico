package es.uji.ei1048.meteorologia.service;

import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.WeatherData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface IWeatherProvider {
    /**
     * @param query Text to search for.
     * @return A list of cities that match the query.
     */
    @NotNull List<@NotNull City> getSuggestedCities(final @NotNull String query);

    /**
     * @param cityName The name of the city to get the id from.
     * @return An optional with the city corresponding to the given city name if existing.
     */
    @NotNull Optional<City> getCity(final @NotNull String cityName);

    /**
     * @param city The city.
     * @return The data for the current weather of the given city.
     * @throws NotFoundException         If the city is not found.
     * @throws ConnectionFailedException If an error occurs while connecting to the service.
     * @throws DataParsingException      In an error occurred while parsing the response data from the service.
     */
    @NotNull WeatherData getWeather(final @NotNull City city);

    /**
     * The maximum number of days of forecast information this service can provide.
     *
     * @return An integer between 1 and {@link Integer#MAX_VALUE}
     */
    int getMaxForecastDays();

    /**
     * @param city   The city.
     * @param offset The number of days between today and the day to check the forecast. Must be less or equal to {@link IWeatherProvider#getMaxForecastDays()}.
     * @return A list of data for the forecast of the given city in the given day in the future.
     * @throws IllegalArgumentException  If the number of days is equal or less than zero.
     * @throws NotFoundException         If the city is not found.
     * @throws ConnectionFailedException If an error occurs while connecting to the service.
     * @throws DataParsingException      In an error occurred while parsing the response data from the service.
     */
    @NotNull List<@NotNull WeatherData> getForecast(final @NotNull City city, final int offset);

    /**
     * @param city   The city.
     * @param offset The number of days between today and the days to check the forecast for.
     * @param count  The number of days to check the forecast for.
     * @return A list of data for the forecast of the given city in the given days in the future.
     * @throws IllegalArgumentException  If the number of days is equal or less than zero.
     * @throws NotFoundException         If the city is not found.
     * @throws ConnectionFailedException If an error occurs while connecting to the service.
     * @throws DataParsingException      In an error occurred while parsing the response data from the service.
     */
    @NotNull List<@NotNull WeatherData> getForecast(final @NotNull City city, final int offset, final int count);
}
