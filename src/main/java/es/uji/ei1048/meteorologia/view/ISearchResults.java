package es.uji.ei1048.meteorologia.view;

import es.uji.ei1048.meteorologia.App;
import es.uji.ei1048.meteorologia.model.WeatherData;
import org.jetbrains.annotations.NotNull;

public interface ISearchResults {

    void showResults(final @NotNull WeatherData wd);

    void save();

    void setApp(final App app);
}
