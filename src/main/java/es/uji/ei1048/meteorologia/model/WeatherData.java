package es.uji.ei1048.meteorologia.model;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.StringJoiner;

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

    public WeatherData(
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

}
