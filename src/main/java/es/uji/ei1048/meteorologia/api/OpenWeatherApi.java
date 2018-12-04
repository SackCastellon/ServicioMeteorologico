package es.uji.ei1048.meteorologia.api;

import com.google.gson.Gson;
import es.uji.ei1048.meteorologia.model.WeatherData;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

public class OpenWeatherApi implements IWeatherApi {

    private static final String URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "44b7cad45f4fb36eefa0f72259b8beb4";

    @Override
    public @NotNull WeatherData getWeather(@NotNull String cityName) throws NotFoundException {
        HttpClient client = HttpClients.createDefault();
        try {
            URI uri = new URIBuilder(URL)
                    .setParameter("q", cityName)
                    .setParameter("appid", API_KEY)
                    .build();

            HttpGet request = new HttpGet(uri);
            HttpResponse response = client.execute(request);

            ApiUtils.checkStatus(response.getStatusLine());

            Gson gson = new Gson();

            return gson.fromJson(EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8")), WeatherData.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new NullPointerException(); //FIXME
    }

    @Override
    public @NotNull WeatherData getForecast(@NotNull String cityName, int days) {
        throw new UnsupportedOperationException();
    }
}
