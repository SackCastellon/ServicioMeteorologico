package es.uji.ei1048.meteorologia.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import es.uji.ei1048.meteorologia.model.WeatherData;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class OpenWeatherApi implements IWeatherApi {

    private static final @NotNull String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather"; //NON-NLS
    private static final @NotNull String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast"; //NON-NLS
    private static final @NotNull String API_KEY = "44b7cad45f4fb36eefa0f72259b8beb4"; //NON-NLS

    /**
     * @param cityName The name of the city to  check the weather
     * @param url      The url of the weather service
     * @return The response from the weather service
     * @throws NotFoundException         If the city is not found
     * @throws ConnectionFailedException If an error occurs while connecting to the service
     */
    private static @NotNull HttpResponse getHttpResponse(final @NotNull String cityName, final @NotNull String url) {
        try (final @NotNull CloseableHttpClient client = HttpClients.createDefault()) {
            final @NotNull URI uri = new URIBuilder(url)
                    .setParameter("q", cityName) //NON-NLS
                    .setParameter("appid", API_KEY) //NON-NLS
                    .build();

            final @NotNull HttpUriRequest request = new HttpGet(uri);
            final @NotNull HttpResponse response = client.execute(request);

            ApiUtils.checkStatus(response.getStatusLine());

            return response;
        } catch (final @NotNull URISyntaxException | IOException e) {
            throw new ConnectionFailedException(e.getMessage());
        }
    }

    @Override
    public @NotNull WeatherData getWeather(final @NotNull String cityName) {
        try {
            final @NotNull HttpResponse response = getHttpResponse(cityName, WEATHER_URL);
            final @NotNull Gson gson = new Gson();
            final @NotNull String entity = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
            return gson.fromJson(entity, WeatherData.class);
        } catch (final @NotNull IOException e) {
            throw new DataParsingException(e.getMessage());
        }
    }

    @Override
    public @NotNull List<@NotNull WeatherData> getForecast(final @NotNull String cityName, final int days) {
        try {
            final @NotNull HttpResponse response = getHttpResponse(cityName, FORECAST_URL);
            final @NotNull String entity = EntityUtils.toString(response.getEntity());
            final @NotNull JsonArray list = new JsonParser().parse(entity).getAsJsonObject().getAsJsonArray("list"); //NON-NLS
            final @NotNull Gson gson = new Gson();
            return StreamSupport.stream(list.spliterator(), false)
                    .map(it -> gson.fromJson(it, WeatherData.class))
                    .collect(Collectors.toList());
        } catch (final @NotNull IOException e) {
            throw new DataParsingException(e.getMessage());
        }
    }
}
