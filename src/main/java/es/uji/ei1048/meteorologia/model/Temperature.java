package es.uji.ei1048.meteorologia.model;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.StringJoiner;

public final class Temperature {

    @NonNls
    private final double current;
    @NonNls
    private final double min;
    @NonNls
    private final double max;

    public Temperature(final double current, final double min, final double max) {
        this.current = current;
        this.min = min;
        this.max = max;
    }

    public double getCurrent() {
        return current;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Temperature)) return false;
        final @NotNull Temperature that = (Temperature) obj;
        return Double.compare(that.current, current) == 0 &&
                Double.compare(that.min, min) == 0 &&
                Double.compare(that.max, max) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(current, min, max);
    }

    @Override
    public @NotNull String toString() {
        return new StringJoiner(", ", Temperature.class.getSimpleName() + "[", "]")
                .add("current=" + current) //NON-NLS
                .add("min=" + min) //NON-NLS
                .add("max=" + max) //NON-NLS
                .toString();
    }
}
