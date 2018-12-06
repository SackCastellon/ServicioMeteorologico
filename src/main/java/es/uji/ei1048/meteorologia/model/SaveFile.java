package es.uji.ei1048.meteorologia.model;

public class SaveFile {

    private WeatherData wd;
    private boolean advanced;

    public WeatherData getWd() {
        return wd;
    }

    public void setWd(final WeatherData wd) {
        this.wd = wd;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public void setAdvanced(final boolean advanced) {
        this.advanced = advanced;
    }
}
