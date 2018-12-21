package es.uji.ei1048.meteorologia.service;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import es.uji.ei1048.meteorologia.api.ApiUtils;
import es.uji.ei1048.meteorologia.api.ConnectionFailedException;
import es.uji.ei1048.meteorologia.api.NotFoundException;
import es.uji.ei1048.meteorologia.model.*;
import es.uji.ei1048.meteorologia.model.converter.CityStringConverter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import static java.util.Spliterator.*;
import static kotlin.collections.CollectionsKt.binarySearchBy;
import static kotlin.collections.CollectionsKt.emptyList;

public final class OpenWeather extends AbstractWeatherProvider {

    private static final @NotNull Logger logger = LogManager.getLogger(OpenWeather.class);

    private static final @NotNull IWeatherProvider INSTANCE = new OpenWeather();

    private static final @NotNull String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather"; //NON-NLS
    private static final @NotNull String FORECAST_URL = "https://api.openweathermap.org/data/2.5/forecast"; //NON-NLS
    private static final @NotNull String API_KEY = "44b7cad45f4fb36eefa0f72259b8beb4"; //NON-NLS
    private static final @NotNull String CITIES_JSON = "/json/openweather.cities.json"; //NON-NLS

    private static final @NotNull TypeAdapter<WeatherData> ADAPTER = new Adapter();

    private static final @NotNull ImmutableMap<@NotNull Long, @NotNull City> CITIES;
    private static final @NotNull LoadingCache<@NotNull String, @NotNull List<@NotNull City>> SUGGESTIONS;

    private static final int MAX_FORECAST_DAYS = 5;

    static {
        final long start = System.currentTimeMillis();

        @NotNull JsonArray jsonArray = new JsonArray(0);

        try (final InputStream stream = OpenWeather.class.getResourceAsStream(CITIES_JSON);
             final InputStreamReader reader = new InputStreamReader(stream, Charsets.UTF_8)) {
            jsonArray = new JsonParser().parse(reader).getAsJsonArray();
        } catch (final IOException e) {
            logger.error(String.format("Failed to read '%s'", CITIES_JSON), e); //NON-NLS
        }

        final @NotNull Spliterator<JsonElement> spliterator = Spliterators.spliterator(
                jsonArray.iterator(),
                (long) jsonArray.size(),
                SIZED | SUBSIZED | ORDERED | DISTINCT | NONNULL
        );

        CITIES = StreamSupport.stream(spliterator, false)
                .map(JsonElement::getAsJsonObject)
                .map(it -> new City(
                        it.get("id").getAsLong(), //NON-NLS
                        it.get("name").getAsString(), //NON-NLS
                        it.get("country").getAsString() //NON-NLS
                ))
                .sorted(Comparator.comparing(City::getName))
                .collect(ImmutableMap.toImmutableMap(City::getId, city -> city));

        final long end = System.currentTimeMillis() - start;
        logger.debug(() -> String.format("Loaded %d cities in %d ms", CITIES.size(), end)); //NON-NLS

        SUGGESTIONS = Caffeine.newBuilder()
                .maximumSize(100_000L)
                .initialCapacity(1_000)
                .build(query -> CITIES
                        .values().asList()
                        .parallelStream()
                        .filter(it -> !it.getName().trim().isEmpty())
                        .filter(it -> Math.abs(it.getName().length() - query.length()) < 5)
                        .sorted(getCityQueryComparator(query))
                        .limit(SUGGESTION_COUNT)
                        .collect(Collectors.toList()));
    }

    private OpenWeather() {
    }

    public static @NotNull IWeatherProvider getInstance() {
        return INSTANCE;
    }

    /**
     * @param cityId The id of the city.
     * @param url    The url of the weather service.
     * @return The response from the weather service.
     * @throws NotFoundException         If the city is not found.
     * @throws ConnectionFailedException If an error occurs while connecting to the service.
     */
    private static @NotNull String getJsonResponse(final long cityId, @NonNls final @NotNull String url) {
        try (final @NotNull CloseableHttpClient client = HttpClients.createDefault()) {
            final @NotNull URI uri = new URIBuilder(url)
                    .setParameter("id", Long.toString(cityId)) //NON-NLS
                    .setParameter("appid", API_KEY) //NON-NLS
                    .build();

            final @NotNull HttpUriRequest request = new HttpGet(uri);

            try (final @NotNull CloseableHttpResponse response = client.execute(request)) {
                ApiUtils.checkStatus(response.getStatusLine());
                return EntityUtils.toString(response.getEntity());
            }
        } catch (final @NotNull URISyntaxException | IOException e) {
            throw new ConnectionFailedException(e.getMessage());
        }
    }

    @Override
    public @NotNull List<@NotNull City> getSuggestedCities(@NonNls final @NotNull String query) {
        if (query.isEmpty()) return emptyList();
        final long start = System.currentTimeMillis();
        final @NotNull List<@NotNull City> cities = Objects.requireNonNull(SUGGESTIONS.get(query.toLowerCase(Locale.ENGLISH)));
        final long end = System.currentTimeMillis() - start;
        logger.debug(() -> String.format("Generated %d suggestions for '%s' in %d ms", SUGGESTION_COUNT, query, end)); //NON-NLS
        return cities;
    }

    @Override
    public @NotNull Optional<City> getCity(final @NotNull String cityName) {
        final int i = binarySearchBy(CITIES.values().asList(),
                cityName.toLowerCase(Locale.ENGLISH),
                0,
                CITIES.size() - 1,
                city -> CityStringConverter.getInstance().toString(city).toLowerCase(Locale.ENGLISH));
        return i < 0 ? Optional.empty() : Optional.of(CITIES.values().asList().get(i));
    }

    @Override
    public @NotNull WeatherData getWeather(final @NotNull City city) {
        final @NotNull String response = getJsonResponse(city.getId(), WEATHER_URL);
        final @NotNull Gson gson = new GsonBuilder()
                .registerTypeAdapter(WeatherData.class, ADAPTER)
                .create();

        return gson.fromJson(response, WeatherData.class);
    }

    @Override
    public int getMaxForecastDays() {
        return MAX_FORECAST_DAYS;
    }

    @Override
    public @NotNull List<@NotNull WeatherData> getForecast(final @NotNull City city, final int offset, final int count) {
        if (offset <= 0) throw new IllegalArgumentException("Day offset must be greater than 0");
        if (count <= 0) throw new IllegalArgumentException("Day count must be greater than 0");
        if (offset + count > MAX_FORECAST_DAYS + 1)
            throw new IllegalArgumentException("The offset and count days represent a day greater than the supported by this service");

        final @NotNull String response = getJsonResponse(city.getId(), FORECAST_URL);

        final @NotNull JsonObject json = new JsonParser().parse(response).getAsJsonObject();

        final int cityId = json.get("city").getAsJsonObject().get("id").getAsInt(); //NON-NLS

        final @NotNull Gson gson = new GsonBuilder()
                .registerTypeAdapter(WeatherData.class, ADAPTER)
                .create();

        final @NotNull LocalDate now = ZonedDateTime.now().withZoneSameLocal(ZoneOffset.UTC).toLocalDate().plusDays((long) offset);

        return StreamSupport.stream(json.getAsJsonArray("list").spliterator(), false) //NON-NLS
                .filter(it -> {
                    final long timestamp = it.getAsJsonObject().get("dt").getAsLong(); //NON-NLS
                    final @NotNull Instant instant = Instant.ofEpochSecond(timestamp);
                    final @NotNull LocalDate time = LocalDateTime.ofInstant(instant, ZoneOffset.UTC).toLocalDate();
                    return time.compareTo(now) >= 0 && time.compareTo(now.plusDays((long) (count - 1))) <= 0;
                })
                .peek(it -> it.getAsJsonObject().addProperty("id", cityId)) //NON-NLS
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

            long cityId = -1L;
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
                        weather = new Weather(
                                id,
                                Objects.requireNonNull(main),
                                Objects.requireNonNull(description)
                        );
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

                case "id": //NON-NLS
                    cityId = in.nextLong();
                    break;

                default:
                    in.skipValue();
                    break;
            }

            in.endObject();

            return new WeatherData(
                    CITIES.get(cityId),
                    Objects.requireNonNull(dateTime),
                    Objects.requireNonNull(weather),
                    Objects.requireNonNull(temperature),
                    Objects.requireNonNull(wind),
                    pressure,
                    humidity
            );
        }
    }
}
