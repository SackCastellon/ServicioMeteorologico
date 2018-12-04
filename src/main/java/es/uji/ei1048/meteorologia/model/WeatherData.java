package es.uji.ei1048.meteorologia.model;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

//import com.google.gson.annotations.JsonAdapter;

//@JsonAdapter(WeatherData.Adapter.class)
public final class WeatherData {

    private final @NotNull Coordinates coord;
    private final @NotNull List<Weather> weather;
    private final @NotNull Temperature temperature;
    private final @NotNull Wind wind;

    private final float pressure;
    private final float humidity;

    public WeatherData(@NotNull Coordinates coord, @NotNull List<Weather> weather, @NotNull Temperature temperature, @NotNull Wind wind, float pressure, float humidity) {
        this.coord = coord;
        this.weather = weather;
        this.temperature = temperature;
        this.wind = wind;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public @NotNull Coordinates getCoord() {
        return coord;
    }

    public @NotNull List<Weather> getWeather() {
        return weather;
    }

    public @NotNull Temperature getTemperature() {
        return temperature;
    }

    public @NotNull Wind getWind() {
        return wind;
    }

    public float getPressure() {
        return pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public class Adapter extends TypeAdapter<WeatherData> {
        @Override
        public void write(JsonWriter out, WeatherData value) throws IOException {
            throw new UnsupportedOperationException(); // TODO
        }

        @Override
        public WeatherData read(JsonReader in) throws IOException {
            throw new UnsupportedOperationException(); // TODO
        }
    }
}
