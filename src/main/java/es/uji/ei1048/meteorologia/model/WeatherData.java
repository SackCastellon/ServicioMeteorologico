package es.uji.ei1048.meteorologia.model;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;
import java.util.StringJoiner;

@JsonAdapter(WeatherData.Adapter.class)
public final class WeatherData {

    @NonNls
    private final @NotNull Weather weather;
    @NonNls
    private final @NotNull Temperature temperature;
    @NonNls
    private final @NotNull Wind wind;
    @NonNls
    private final double pressure;
    @NonNls
    private final double humidity;

    private WeatherData(
            final @NotNull Weather weather,
            final @NotNull Temperature temperature,
            final @NotNull Wind wind,
            final double pressure,
            final double humidity
    ) {
        this.weather = weather;
        this.temperature = temperature;
        this.wind = wind;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public @NotNull Weather getWeather() {
        return weather;
    }

    public @NotNull Temperature getTemperature() {
        return temperature;
    }

    public @NotNull Wind getWind() {
        return wind;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof WeatherData)) return false;
        final @NotNull WeatherData that = (WeatherData) obj;
        return Double.compare(that.pressure, pressure) == 0 &&
                Double.compare(that.humidity, humidity) == 0 &&
                weather.equals(that.weather) &&
                temperature.equals(that.temperature) &&
                wind.equals(that.wind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weather, temperature, wind, pressure, humidity);
    }

    @Override
    public @NotNull String toString() {
        return new StringJoiner(", ", WeatherData.class.getSimpleName() + "[", "]")
                .add("weather=" + weather) //NON-NLS
                .add("temperature=" + temperature) //NON-NLS
                .add("wind=" + wind) //NON-NLS
                .add("pressure=" + pressure) //NON-NLS
                .add("humidity=" + humidity) //NON-NLS
                .toString();
    }

    static final class Adapter extends TypeAdapter<WeatherData> {
        @Override
        @Contract("_, _ -> fail")
        public void write(final JsonWriter out, final WeatherData value) {
            throw new UnsupportedOperationException("Serialization toJSON still not supported.");
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

            while (in.hasNext()) {
                switch (in.nextName()) {
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
                        newTemperature = new Temperature(temp, tempMin, tempMax);
                        break;
                    case "wind": //NON-NLS
                        in.beginObject(); // Begin "wind"

                        double speed = Double.NaN;
                        double deg = Double.NaN;
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
                        if (Double.isNaN(deg)) throw new IllegalStateException("No 'wind.deg' was found.");
                        newWind = new Wind(speed, deg);
                        break;
                    default:
                        in.skipValue();
                        break;
                }
            }

            in.endObject();

            if (newWeather == null) throw new IllegalStateException("No 'weather' was found.");
            if (newTemperature == null) throw new IllegalStateException("No 'main' was found.");
            if (newWind == null) throw new IllegalStateException("No 'wind' was found.");

            return new WeatherData(newWeather, newTemperature, newWind, newPressure, newHumidity);
        }
    }
}
