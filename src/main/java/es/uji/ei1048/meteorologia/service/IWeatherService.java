package es.uji.ei1048.meteorologia.service;

import es.uji.ei1048.meteorologia.api.ConnectionFailedException;
import es.uji.ei1048.meteorologia.api.DataParsingException;
import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.WeatherData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IWeatherService {
    /**
     * @param city The city
     * @return The data for the current weather of the given city
     * @throws NotFoundException         If the city is not found
     * @throws ConnectionFailedException If an error occurs while connecting to the service
     * @throws DataParsingException      In an error occurred while parsing the response data from the service
     */
    WeatherData getWeather(final String city);

    /**
     * @param city The city
     * @param days The number of days from now to check the forecast
     * @return A list of data for the forecast of the given city in the given days in the future
     * @throws IllegalArgumentException  If the number of days is equal or less than zero
     * @throws NotFoundException         If the city is not found
     * @throws ConnectionFailedException If an error occurs while connecting to the service
     * @throws DataParsingException      In an error occurred while parsing the response data from the service
     */
    @NotNull List<@NotNull WeatherData> getForecast(final @NotNull String city, final int days);
}
