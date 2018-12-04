package es.uji.ei1048.meteorologia.model;

import org.jetbrains.annotations.NotNull;

public final class Weather {
    private final int id;
    private final @NotNull String main;
    private final @NotNull String description;

    public Weather(int id, @NotNull String main, @NotNull String description) {
        this.id = id;
        this.main = main;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public @NotNull String getMain() {
        return main;
    }

    public @NotNull String getDescription() {
        return description;
    }
}
