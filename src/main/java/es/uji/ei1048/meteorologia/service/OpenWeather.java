package es.uji.ei1048.meteorologia.service;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import es.uji.ei1048.meteorologia.api.ApiUtils;
import es.uji.ei1048.meteorologia.api.ConnectionFailedException;
import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static es.uji.ei1048.meteorologia.model.Temperature.Units.KELVIN;

public final class OpenWeather implements IWeatherService {

    private static final @NotNull String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather"; //NON-NLS
    private static final @NotNull String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast"; //NON-NLS
    private static final @NotNull String API_KEY = "44b7cad45f4fb36eefa0f72259b8beb4"; //NON-NLS
    private static final @NotNull TypeAdapter<WeatherData> ADAPTER = new Adapter();

    /**
     * @param cityName The name of the city to check the weather
     * @param url      The url of the weather service
     * @return The response from the weather service
     * @throws NotFoundException         If the city is not found
     * @throws ConnectionFailedException If an error occurs while connecting to the service
     */
    private static @NotNull String getJsonResponse(final @NotNull String cityName, final @NotNull String url) {
        try (final @NotNull CloseableHttpClient client = HttpClients.createDefault()) {
            final @NotNull URI uri = new URIBuilder(url)
                    .setParameter("q", cityName) //NON-NLS
                    .setParameter("appid", API_KEY) //NON-NLS
                    .build();

            final @NotNull HttpUriRequest request = new HttpGet(uri);
            final @NotNull HttpResponse response = client.execute(request);

            ApiUtils.checkStatus(response.getStatusLine());

            return EntityUtils.toString(response.getEntity());
        } catch (final @NotNull URISyntaxException | IOException e) {
            throw new ConnectionFailedException(e.getMessage());
        }
    }

    @Override
    public @NotNull WeatherData getWeather(final @NotNull City city) {
        final @NotNull String response = getJsonResponse(city.getName(), WEATHER_URL);
        final @NotNull Gson gson = new GsonBuilder()
                .registerTypeAdapter(WeatherData.class, ADAPTER)
                .create();

        return gson.fromJson(response, WeatherData.class);
    }

    @Override
    public @NotNull List<@NotNull WeatherData> getForecast(final @NotNull City city, final int days) {
        if (days <= 0) throw new IllegalArgumentException("days must be greater than 0");

        final @NotNull String response = getJsonResponse(city.getName(), FORECAST_URL);
        final @NotNull JsonArray list = new JsonParser()
                .parse(response)
                .getAsJsonObject()
                .getAsJsonArray("list"); //NON-NLS

        final @NotNull Gson gson = new GsonBuilder()
                .registerTypeAdapter(WeatherData.class, ADAPTER)
                .create();

        return StreamSupport.stream(list.spliterator(), false)
                .map(it -> gson.fromJson(it, WeatherData.class))
                .collect(Collectors.toList());
    }

    private static final class Adapter extends TypeAdapter<WeatherData> {
        @Override
        @Contract("_, _ -> fail")
        public void write(final JsonWriter out, final WeatherData value) {
            throw new UnsupportedOperationException("This adapter is only intended to be used for deserialization");
        }

        @Override
        @Contract("_ -> new")
        public @NotNull WeatherData read(final JsonReader in) throws IOException {
            in.beginObject();

            Weather newWeather = null;
            Temperature newTemperature = null;
            Wind newWind = null;
            double newPressure = Double.NaN;
            double newHumidity = Double.NaN;

            while (in.hasNext()) switch (in.nextName()) {
                case "weather": //NON-NLS
                    in.beginArray();

                    if (in.hasNext()) { // Only save the first "weather"
                        in.beginObject(); // Begin "weather"

                        int id = -1;
                        String main = null;
                        String description = null;
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "id": //NON-NLS
                                    id = in.nextInt();
                                    break;
                                case "main": //NON-NLS
                                    main = in.nextString();
                                    break;
                                case "description": //NON-NLS
                                    description = in.nextString();
                                    break;
                                default:
                                    in.skipValue();
                                    break;
                            }
                        }

                        in.endObject(); // End "weather"

                        if (id == -1) throw new IllegalStateException("No 'weather.id' was found.");
                        if (main == null) throw new IllegalStateException("No 'weather.main' was found.");
                        if (description == null)
                            throw new IllegalStateException("No 'weather.description' was found.");
                        newWeather = new Weather(id, main, description);
                    }

                    in.endArray();
                    break;
                case "main": //NON-NLS
                    in.beginObject(); // Begin "main"


                    double temp = Double.NaN;
                    double tempMin = Double.NaN;
                    double tempMax = Double.NaN;
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "temp": //NON-NLS
                                temp = in.nextDouble();
                                break;
                            case "temp_min": //NON-NLS
                                tempMin = in.nextDouble();
                                break;
                            case "temp_max": //NON-NLS
                                tempMax = in.nextDouble();
                                break;
                            case "pressure": //NON-NLS
                                newPressure = in.nextDouble();
                                break;
                            case "humidity": //NON-NLS
                                newHumidity = in.nextDouble();
                                break;
                            default:
                                in.skipValue();
                                break;
                        }
                    }

                    in.endObject(); // End "main"

                    if (Double.isNaN(temp)) throw new IllegalStateException("No 'main.temp' was found.");
                    if (Double.isNaN(tempMin)) throw new IllegalStateException("No 'main.temp_min' was found.");
                    if (Double.isNaN(tempMax)) throw new IllegalStateException("No 'main.temp_max' was found.");
                    if (Double.isNaN(newPressure)) throw new IllegalStateException("No 'main.pressure' was found.");
                    if (Double.isNaN(newHumidity)) throw new IllegalStateException("No 'main.humidity' was found.");
                    newTemperature = new Temperature(temp, tempMin, tempMax, KELVIN);
                    break;
                case "wind": //NON-NLS
                    in.beginObject(); // Begin "wind"

                    double speed = Double.NaN;
                    double deg = -1.0;
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "speed": //NON-NLS
                                speed = in.nextDouble();
                                break;
                            case "deg":
                                deg = in.nextDouble();
                                break;
                            default:
                                in.skipValue();
                                break;
                        }
                    }

                    in.endObject(); // End "wind"

                    if (Double.isNaN(speed)) throw new IllegalStateException("No 'wind.speed' was found.");
                    newWind = new Wind(speed, deg);
                    break;
                default:
                    in.skipValue();
                    break;
            }

            in.endObject();

            if (newWeather == null) throw new IllegalStateException("No 'weather' was found.");
            if (newTemperature == null) throw new IllegalStateException("No 'main' was found.");
            if (newWind == null) throw new IllegalStateException("No 'wind' was found.");

            return new WeatherData(null, newWeather, newTemperature, newWind, newPressure, newHumidity);
        }
    }
}
