package es.uji.ei1048.meteorologia;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Scanner;

public class Weather {
    public static void doHttpGet() {

        Scanner scanner = new Scanner(System.in);
        //System.out.print("Dime el ID de la ciudad: ");
        //String cityID = scanner.nextLine();
        System.out.print("Dime el nombre de la ciudad: ");
        String cityName = scanner.nextLine();
        //cityName.replace(" ","%20");

        //String url = String.format("http://api.openweathermap.org/data/2.5/weather?id=%s&appid=%s", cityID, getApiKey());  //tiempo actual ID
        //String url = String.format("http://api.openweathermap.org/data/2.5/forecast?id=%s&appid=%s", cityID, getApiKey());   //rango 5 dias ID
        String url = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s,es&APPID=%s", cityName, getApiKey()); //tiempo actual nombre
        //String url = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s,es&APPID=%s", cityName, getApiKey()); //rango 5 dias nombre

        //String url = String.format("http://api.openweathermap.org/data/2.5/weather?q=benicassim,es&APPID=%s", getApiKey()); //rango 5 dias prueba
        //String url = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/348735?apikey=1qnDGxmkUObV0hjDGUf8KbpWSYndly7e";  //prueba otra API

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
        String password = "44b7cad45f4fb36eefa0f72259b8beb4";  //ApiKey openweathermap
        //String password = "1qnDGxmkUObV0hjDGUf8KbpWSYndly7e"; //ApiKey accuweather
        return password;
    }

}
