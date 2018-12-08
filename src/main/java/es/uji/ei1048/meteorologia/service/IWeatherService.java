package es.uji.ei1048.meteorologia.service;

import es.uji.ei1048.meteorologia.api.ConnectionFailedException;
import es.uji.ei1048.meteorologia.api.DataParsingException;
import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.WeatherData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.OptionalInt;

public interface IWeatherService {
    /**
     * @param query Text to search for.
     * @return A list of cities that match the query.
     */
    @NotNull List<@NotNull String> getSuggestedCities(final @NotNull String query);

    /**
     * @param query Text to search for.
     * @param count The maximum size of the suggestion list returned.
     * @return A list of cities that match the query.
     */
    @NotNull List<@NotNull String> getSuggestedCities(final @NotNull String query, final int count);

    /**
     * @param cityName The name of the city to get the id from.
     * @return An optional integer representing the ID id the city specified.
     */
    @NotNull OptionalInt getCityId(final @NotNull String cityName);

    /**
     * @param cityId The id of the city.
     * @return The data for the current weather of the given city.
     * @throws NotFoundException         If the city is not found.
     * @throws ConnectionFailedException If an error occurs while connecting to the service.
     * @throws DataParsingException      In an error occurred while parsing the response data from the service.
     */
    @NotNull WeatherData getWeather(final int cityId);

    /**
     * @param cityId The id of the city.
     * @param offset The number of days between today and the day to check the forecast.
     * @return A list of data for the forecast of the given city in the given day in the future.
     * @throws IllegalArgumentException  If the number of days is equal or less than zero.
     * @throws NotFoundException         If the city is not found.
     * @throws ConnectionFailedException If an error occurs while connecting to the service.
     * @throws DataParsingException      In an error occurred while parsing the response data from the service.
     */
    @NotNull List<@NotNull WeatherData> getForecast(final int cityId, final int offset);

    /**
     * @param cityId The id of the city.
     * @param offset The number of days between today and the days to check the forecast for.
     * @param count  The number of days to check the forecast for.
     * @return A list of data for the forecast of the given city in the given days in the future.
     * @throws IllegalArgumentException  If the number of days is equal or less than zero.
     * @throws NotFoundException         If the city is not found.
     * @throws ConnectionFailedException If an error occurs while connecting to the service.
     * @throws DataParsingException      In an error occurred while parsing the response data from the service.
     */
    @NotNull List<@NotNull WeatherData> getForecast(final int cityId, final int offset, final int count);
}
