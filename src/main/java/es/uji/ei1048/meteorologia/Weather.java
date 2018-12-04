package es.uji.ei1048.meteorologia;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Weather {
    public static void doHttpGet() {
        //String url = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/348735?apikey=1qnDGxmkUObV0hjDGUf8KbpWSYndly7e";
        String url = "http://api.openweathermap.org/data/2.5/weather?q=benicassim,es&APPID=44b7cad45f4fb36eefa0f72259b8beb4";
        //String url = "http://api.openweathermap.org/data/2.5/forecast?id=524901&APPID=" + getApiKey();

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);

        try{
            CloseableHttpResponse resp = client.execute(get);
            HttpEntity entity = resp.getEntity();
            System.out.println("Json response");
            System.out.println(EntityUtils.toString(entity));

        }catch (IOException ioe) {
            System.err.println("Something went wrong getting the weather");
            ioe.printStackTrace();
        }catch (Exception e) {
            System.err.println("Error");
            e.printStackTrace();
        }

    }

    public static String getApiKey() {
        String password = "44b7cad45f4fb36eefa0f72259b8beb4";
        return password;
    }

}
