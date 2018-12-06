package es.uji.ei1048.meteorologia.model;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.StringJoiner;

public final class City {
    @NonNls
    private final int id;
    @NonNls
    private final @NotNull String name;
    @NonNls
    private final @NotNull String country;
    @NonNls
    private final @NotNull Coordinates coordinates;

    public City(
            final int id,
            final @NotNull String name,
            final @NotNull String country,
            final @NotNull Coordinates coordinates
    ) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.coordinates = coordinates;
    }

    public int getId() {
        return id;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String getCountry() {
        return country;
    }

    public @NotNull Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof City)) return false;
        final @NotNull City city = (City) obj;
        return id == city.id &&
                name.equals(city.name) &&
                country.equals(city.country) &&
                coordinates.equals(city.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, country, coordinates);
    }

    @Override
    public @NotNull String toString() {
        return new StringJoiner(", ", City.class.getSimpleName() + "[", "]")
                .add("id=" + id) //NON-NLS
                .add("name='" + name + "'") //NON-NLS
                .add("country='" + country + "'") //NON-NLS
                .add("coordinates=" + coordinates) //NON-NLS
                .toString();
    }
}
