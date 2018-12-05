package es.uji.ei1048.meteorologia.model;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.StringJoiner;

public final class Weather {

    @NonNls
    private final int id;
    @NonNls
    private final @NotNull String main;
    @NonNls
    private final @NotNull String description;

    public Weather(final int id, final @NotNull String main, final @NotNull String description) {
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

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Weather)) return false;
        final @Nullable Weather weather = (Weather) obj;
        return id == weather.id &&
                main.equals(weather.main) &&
                description.equals(weather.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, main, description);
    }

    @Override
    public @NotNull String toString() {
        return new StringJoiner(", ", Weather.class.getSimpleName() + "[", "]")
                .add("id=" + id) //NON-NLS
                .add("main='" + main + "'") //NON-NLS
                .add("description='" + description + "'") //NON-NLS
                .toString();
    }
}
