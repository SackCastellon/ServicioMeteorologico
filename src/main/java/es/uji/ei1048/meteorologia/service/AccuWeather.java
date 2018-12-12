package es.uji.ei1048.meteorologia.service;

import com.google.gson.TypeAdapter;
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
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

public final class AccuWeather extends AbstractWeatherProvider {

    private static final @NotNull String WEATHER_URL = "http://dataservice.accuweather.com/currentconditions/v1/"; //NON-NLS
    private static final @NotNull String FORECAST_URL = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/"; //NON-NLS
    private static final @NotNull String API_KEY = "1qnDGxmkUObV0hjDGUf8KbpWSYndly7e"; //NON-NLS
    private static final @NotNull TypeAdapter<WeatherData> ADAPTER = new Adapter();

    /**
     * @param cityId The id of the city.
     * @param url    The url of the weather service.
     * @return The response from the weather service.
     * @throws NotFoundException         If the city is not found.
     * @throws ConnectionFailedException If an error occurs while connecting to the service.
     */
    private static @NotNull String getJsonResponse(final long cityId, @NonNls final @NotNull String url) {
        try (final @NotNull CloseableHttpClient client = HttpClients.createDefault()) {
            final @NotNull URI uri = new URIBuilder(url + cityId)
                    .setParameter("apikey", API_KEY) //NON-NLS
                    .setParameter("details", Boolean.toString(true)) //NON-NLS
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
    public @NotNull List<@NotNull City> getSuggestedCities(final @NotNull String query) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull Optional<City> getCity(final @NotNull String cityName) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull WeatherData getWeather(final @NotNull City city) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public int getMaxForecastDays() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull List<@NotNull WeatherData> getForecast(final @NotNull City city, final int offset) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public @NotNull List<@NotNull WeatherData> getForecast(final @NotNull City city, final int offset, final int count) {
        throw new UnsupportedOperationException("Not implemented");
    }

    private static final class Adapter extends TypeAdapter<WeatherData> {
        private static double getMetricValue(final @NotNull JsonReader in) throws IOException {
            in.beginObject();
            double value = Double.NaN;
            while (in.hasNext()) if ("Metric".equals(in.nextName())) { //NON-NLS
                in.beginObject();
                //NON-NLS
                while (in.hasNext()) if ("Value".equals(in.nextName())) { //NON-NLS
                    value = in.nextDouble();
                } else {
                    in.skipValue();
                }
                in.endObject();
            } else {
                in.skipValue();
            }
            in.endObject();
            if (Double.isNaN(value)) throw new IllegalStateException("No metric value was found!");
            return value;
        }

        @Override
        @Contract("_, _ -> fail")
        public void write(final JsonWriter out, final WeatherData value) {
            throw new UnsupportedOperationException("This adapter is only intended to be used for deserialization");
        }

        @Override
        @Contract("_ -> new")
        public @NotNull WeatherData read(final JsonReader in) throws IOException {
            in.beginArray();
            in.beginObject();

            final City city = null; // TODO
            LocalDateTime dateTime = null;
            Weather weather = null;
            Wind wind = null;
            double temp = Double.NaN;
            double tempMin = Double.NaN;
            double tempMax = Double.NaN;
            double pressure = Double.NaN;
            double humidity = Double.NaN;

            while (in.hasNext()) switch (in.nextName()) {
                case "WeatherText": //NON-NLS
                    weather = new Weather(-1, in.nextString(), ""); // FIXME
                    break;
                case "Temperature": //NON-NLS
                    temp = getMetricValue(in);
                    if (Double.isNaN(temp)) throw new IllegalStateException("No temperature was found.");
                    break;
                case "TemperatureSummary": //NON-NLS
                    in.beginObject();
                    while (in.hasNext()) if ("Past24HourRange".equals(in.nextName())) { //NON-NLS
                        in.beginObject();
                        while (in.hasNext()) switch (in.nextName()) {
                            case "Minimum": //NON-NLS
                                tempMin = getMetricValue(in);
                                break;
                            case "Maximum": //NON-NLS
                                tempMax = getMetricValue(in);
                                break;
                            default:
                                in.skipValue();
                                break;
                        }
                        in.endObject();
                    } else {
                        in.skipValue();
                    }
                    in.endObject();

                    if (Double.isNaN(tempMin)) throw new IllegalStateException("No minimum temperature was found.");
                    if (Double.isNaN(tempMax)) throw new IllegalStateException("No maximum temperature was found.");
                    break;
                case "Wind": //NON-NLS
                    in.beginObject();
                    double speed = Double.NaN;
                    double deg = -1.0;
                    while (in.hasNext()) switch (in.nextName()) {
                        case "Speed": //NON-NLS
                            speed = getMetricValue(in);
                            break;
                        case "Direction": //NON-NLS
                            in.beginObject();
                            while (in.hasNext()) if ("Degrees".equals(in.nextName())) {//NON-NLS
                                deg = in.nextDouble();
                            } else {
                                in.skipValue();
                            }
                            in.endObject();
                            break;
                        default:
                            in.skipValue();
                            break;
                    }
                    in.endObject();

                    if (Double.isNaN(speed)) throw new IllegalStateException("No wind speed was found.");
                    wind = new Wind(speed, deg);
                    break;
                case "Pressure": //NON-NLS
                    pressure = getMetricValue(in);
                    if (Double.isNaN(pressure)) throw new IllegalStateException("No pressure was found.");
                    break;
                case "RelativeHumidity": //NON-NLS
                    humidity = in.nextDouble();
                    if (Double.isNaN(temp)) throw new IllegalStateException("No humidity was found.");
                    break;
                case "EpochTime": //NON-NLS
                    dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(in.nextLong()), ZoneOffset.UTC);
                    break;
                default:
                    in.skipValue();
                    break;
            }

            in.endObject();
            in.endArray();

            if (dateTime == null) throw new IllegalStateException("No timestamp was found.");
            if (weather == null) throw new IllegalStateException("No weather was found.");
            if (wind == null) throw new IllegalStateException("No wind was found.");

            final Temperature temperature = new Temperature(temp, tempMin, tempMax, Temperature.Units.CELSIUS);

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
