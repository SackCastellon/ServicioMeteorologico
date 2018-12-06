package es.uji.ei1048.meteorologia.model;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

public final class WeatherData {

    @NonNls
    private final @NotNull City city;
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
    @NonNls
    private final LocalDate time;

    // TODO Add timestamp

    public WeatherData(
            final @NotNull City city, // TODO Make non-nullable
            final @NotNull Weather weather,
            final @NotNull Temperature temperature,
            final @NotNull Wind wind,
            final LocalDate date
    ) {
        this(city, weather, temperature, wind, -1.0, -1.0, date);
    }

    public WeatherData(
            final @NotNull City city, // TODO Make non-nullable
            final @NotNull Weather weather,
            final @NotNull Temperature temperature,
            final @NotNull Wind wind,
            final double pressure,
            final double humidity,
            final LocalDate time
    ) {
        this.city = city;
        this.weather = weather;
        this.temperature = temperature;
        this.wind = wind;
        this.pressure = pressure;
        this.humidity = humidity;
        this.time = time;
    }

    public @NotNull City getCity() {
        return city;
    }

    public @NotNull Weather getWeather() {
        return weather;
    }

    public LocalDate getTime() {
        return time;
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
                city.equals(that.city) &&
                weather.equals(that.weather) &&
                temperature.equals(that.temperature) &&
                wind.equals(that.wind) &&
                time.equals(that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, weather, temperature, wind, pressure, humidity, time);
    }

    @Override
    public @NotNull String toString() {
        return new StringJoiner(", ", WeatherData.class.getSimpleName() + "[", "]")
                .add("city=" + city) //NON-NLS
                .add("time=" + time) //NON-NLS
                .add("weather=" + weather) //NON-NLS
                .add("temperature=" + temperature) //NON-NLS
                .add("wind=" + wind) //NON-NLS
                .add("pressure=" + pressure) //NON-NLS
                .add("humidity=" + humidity) //NON-NLS
                .toString();
    }

}
