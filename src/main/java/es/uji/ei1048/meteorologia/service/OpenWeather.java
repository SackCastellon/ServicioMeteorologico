package es.uji.ei1048.meteorologia.service;

import com.google.common.base.Charsets;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import es.uji.ei1048.meteorologia.api.ApiUtils;
import es.uji.ei1048.meteorologia.api.ConnectionFailedException;
import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.Temperature;
import es.uji.ei1048.meteorologia.model.Weather;
import es.uji.ei1048.meteorologia.model.WeatherData;
import es.uji.ei1048.meteorologia.model.Wind;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static es.uji.ei1048.meteorologia.model.Temperature.Units.KELVIN;

public final class OpenWeather extends AbstractWeatherService {

    private static final @NotNull String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather"; //NON-NLS
    private static final @NotNull String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast"; //NON-NLS
    private static final @NotNull String API_KEY = "44b7cad45f4fb36eefa0f72259b8beb4"; //NON-NLS
    private static final @NotNull TypeAdapter<WeatherData> ADAPTER = new Adapter();

    private static final @NotNull Map<String, Integer> cities;

    static {
        @NotNull Map<String, Integer> result;
        try (final InputStream stream = OpenWeather.class.getResourceAsStream("/city.list.min.json"); //NON-NLS
             final InputStreamReader reader = new InputStreamReader(stream, Charsets.UTF_8)) {
            final JsonElement parse = new JsonParser().parse(reader);
            final JsonArray array = parse.getAsJsonArray();

            result = StreamSupport.stream(array.spliterator(), false)
                    .map(JsonElement::getAsJsonObject)
                    .limit(0L)// FIXME There are duplicated names in "city.list.min.json"
                    .collect(Collectors.toMap(
                            it -> it.get("name").getAsString(), //NON-NLS
                            it -> it.get("id").getAsInt() //NON-NLS
                    ));
        } catch (final IOException e) {
            result = Collections.emptyMap();
        }
        cities = result;
    }

    /**
     * @param cityId The id of the city.
     * @param url    The url of the weather service.
     * @return The response from the weather service.
     * @throws NotFoundException         If the city is not found.
     * @throws ConnectionFailedException If an error occurs while connecting to the service.
     */
    private static @NotNull String getJsonResponse(final int cityId, @NonNls final @NotNull String url) {
        try (final @NotNull CloseableHttpClient client = HttpClients.createDefault()) {
            final @NotNull URI uri = new URIBuilder(url)
                    .setParameter("id", Integer.toString(cityId)) //NON-NLS
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
    public @NotNull List<@NotNull String> getSuggestedCities(final @NotNull String query, final int count) {
        return cities.isEmpty() ? Collections.emptyList() : cities
                .keySet()
                .parallelStream()
                .sorted(Comparator.<String>comparingDouble(it -> jaroWinklerDistance.apply(query, it)).reversed())
                .limit((long) count)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull OptionalInt getCityId(final @NotNull String cityName) {
        return OptionalInt.of(cities.get(cityName));
    }

    @Override
    public @NotNull WeatherData getWeather(final int cityId) {
        final @NotNull String response = getJsonResponse(cityId, WEATHER_URL);
        final @NotNull Gson gson = new GsonBuilder()
                .registerTypeAdapter(WeatherData.class, ADAPTER)
                .create();

        final @NotNull WeatherData data = gson.fromJson(response, WeatherData.class);
        //data.setCity(cityName); // TODO
        return data;
    }

    @Override
    public @NotNull List<@NotNull WeatherData> getForecast(final int cityId, final int offset, final int count) {
        if (offset <= 0) throw new IllegalArgumentException("Day offset must be greater than 0");
        if (count <= 0) throw new IllegalArgumentException("Day count must be greater than 0");

        final @NotNull String response = getJsonResponse(cityId, FORECAST_URL);
        final @NotNull JsonArray list = new JsonParser()
                .parse(response)
                .getAsJsonObject()
                .getAsJsonArray("list"); //NON-NLS

        final @NotNull Gson gson = new GsonBuilder()
                .registerTypeAdapter(WeatherData.class, ADAPTER)
                .create();

        final @NotNull LocalDate now = ZonedDateTime.now().withZoneSameLocal(ZoneOffset.UTC).toLocalDate().plusDays((long) offset);

        return StreamSupport.stream(list.spliterator(), false)
                .filter(it -> {
                    final long timestamp = it.getAsJsonObject().get("dt").getAsLong(); //NON-NLS
                    final @NotNull Instant instant = Instant.ofEpochSecond(timestamp);
                    final @NotNull LocalDate time = LocalDateTime.ofInstant(instant, ZoneOffset.UTC).toLocalDate();
                    return time.compareTo(now) >= 0 && time.compareTo(now.plusDays((long) (count - 1))) <= 0;
                })
                .map(it -> gson.fromJson(it, WeatherData.class))
                //.peek(it -> it.setCity(cityName)) // TODO
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

            LocalDateTime dateTime = null;
            Weather weather = null;
            Temperature temperature = null;
            Wind wind = null;
            double pressure = Double.NaN;
            double humidity = Double.NaN;

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
                        weather = new Weather(id, main, description);
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
                                pressure = in.nextDouble();
                                break;
                            case "humidity": //NON-NLS
                                humidity = in.nextDouble();
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
                    if (Double.isNaN(pressure)) throw new IllegalStateException("No 'main.pressure' was found.");
                    if (Double.isNaN(humidity)) throw new IllegalStateException("No 'main.humidity' was found.");
                    temperature = new Temperature(temp, tempMin, tempMax, KELVIN);
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
                    wind = new Wind(speed, deg);
                    break;

                case "dt": //NON-NLS
                    dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(in.nextLong()), ZoneOffset.UTC);
                    break;
                default:
                    in.skipValue();
                    break;
            }

            in.endObject();

            if (dateTime == null) throw new IllegalStateException("No timestamp was found.");
            if (weather == null) throw new IllegalStateException("No 'weather' was found.");
            if (temperature == null) throw new IllegalStateException("No 'main' was found.");
            if (wind == null) throw new IllegalStateException("No 'wind' was found.");

            return new WeatherData(
                    dateTime,
                    weather,
                    temperature,
                    wind,
                    pressure,
                    humidity
            );
        }
    }
}
