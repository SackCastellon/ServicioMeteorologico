package es.uji.ei1048.meteorologia.model;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.StringJoiner;

public final class City {
    @NonNls
    private final long id;
    @NonNls
    private final @NotNull String name;
    @NonNls
    private final @NotNull String country;

    public City(
            final long id,
            @NonNls final @NotNull String name,
            @NonNls final @NotNull String country
    ) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public long getId() {
        return id;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String getCountry() {
        return country;
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof City)) return false;
        final @NotNull City city = (City) obj;
        return id == city.id &&
                name.equals(city.name) &&
                country.equals(city.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, country);
    }

    @Override
    public @NotNull String toString() {
        return new StringJoiner(", ", City.class.getSimpleName() + "[", "]")
                .add("id=" + id) //NON-NLS
                .add("name='" + name + "'") //NON-NLS
                .add("country='" + country + "'") //NON-NLS
                .toString();
    }
}
