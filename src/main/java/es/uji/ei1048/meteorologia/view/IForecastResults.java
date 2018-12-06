package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.WeatherData;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public interface IForecastResults {

    void showForecastResults(final @NotNull City city, final @NotNull List<WeatherData> wdList);

    default void save() {
        throw new NotImplementedException();
    }

}
