package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.model.City;
import es.uji.ei1048.meteorologia.model.WeatherData;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface ISearchResults {
    void showResults(final @NotNull City city, final @NotNull WeatherData wd);

    default void save() {
        throw new NotImplementedException();
    }
}