package es.uji.ei1048.meteorologia.model;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.StringJoiner;

public final class Wind {

    @NonNls
    private final double speed;
    @NonNls
    private final double degrees;

    public Wind(final double speed, final double degrees) {
        this.speed = speed;
        this.degrees = degrees;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDegrees() {
        return degrees;
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Wind)) return false;
        final @NotNull Wind wind = (Wind) obj;
        return Double.compare(wind.speed, speed) == 0 &&
                Double.compare(wind.degrees, degrees) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(speed, degrees);
    }

    @Override
    public @NotNull String toString() {
        return new StringJoiner(", ", Wind.class.getSimpleName() + "[", "]")
                .add("speed=" + speed) //NON-NLS
                .add("degrees=" + degrees) //NON-NLS
                .toString();
    }
}
