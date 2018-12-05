package es.uji.ei1048.meteorologia.model;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.StringJoiner;

public final class Coordinates {

    @NonNls
    private final double longitude;
    @NonNls
    private final double latitude;

    public Coordinates(final double longitude, final double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Coordinates)) return false;
        final @NotNull Coordinates that = (Coordinates) obj;
        return Double.compare(that.longitude, longitude) == 0 &&
                Double.compare(that.latitude, latitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(longitude, latitude);
    }

    @Override
    public @NotNull String toString() {
        return new StringJoiner(", ", Coordinates.class.getSimpleName() + "[", "]")
                .add("longitude=" + longitude) //NON-NLS
                .add("latitude=" + latitude) //NON-NLS
                .toString();
    }
}
