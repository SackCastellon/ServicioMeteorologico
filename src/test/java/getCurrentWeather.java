import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;



public class getCurrentWeather {

    @BeforeAll
    public void loadController(){
        IWeatherAPI api = new OpenWeatherAPI();
    }

    @Test
    public void getCurrentWeather_validCity_suc(){
        String city = "Castellón de la Plana";
        assertEquals(200,api.getCurrentWeather(city).get("cod").getAsInt());
    }

    @Test
    public void getCurrentWeather_notValidCity_err(){
        String city = "Wakanda";
        assertEquals(404,api.getCurrentWeather(city).get("cod").getAsInt());
    }

}
