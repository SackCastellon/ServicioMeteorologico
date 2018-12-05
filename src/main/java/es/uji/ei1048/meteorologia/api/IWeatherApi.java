package es.uji.ei1048.meteorologia.api;

import es.uji.ei1048.meteorologia.model.WeatherData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IWeatherApi {
    /**
     * @param cityName The name of the city
     * @return The data for the current weather of the given city
     * @throws NotFoundException         If the city is not found
     * @throws ConnectionFailedException If an error occurs while connecting to the service
     * @throws DataParsingException      In an error occurred while parsing the response data from the service
     */
    @NotNull WeatherData getWeather(@NotNull String cityName);

    /**
     * @param cityName The name of the city
     * @param days     The number of days from now to check the forecast
     * @return A list of data for the forecast of the given city in the given days in the future
     * @throws NotFoundException         If the city is not found
     * @throws ConnectionFailedException If an error occurs while connecting to the service
     * @throws DataParsingException      In an error occurred while parsing the response data from the service
     */
    @NotNull List<@NotNull WeatherData> getForecast(@NotNull String cityName, int days);
}
