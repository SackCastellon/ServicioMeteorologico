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
    @NonNls
    private final @NotNull Temperature.Units units;

    public Temperature(final double current, final double min, final double max, final @NotNull Temperature.Units units) {
        this.current = current;
        this.min = min;
        this.max = max;
        this.units = units;
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

    public @NotNull Units getUnits() {
        return units;
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Temperature)) return false;
        final @NotNull Temperature that = (Temperature) obj;
        return Double.compare(that.current, current) == 0 &&
                Double.compare(that.min, min) == 0 &&
                Double.compare(that.max, max) == 0 &&
                units.equals(that.units);
    }

    @Override
    public int hashCode() {
        return Objects.hash(current, min, max, units);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Temperature.class.getSimpleName() + "[", "]")
                .add("current=" + current) //NON-NLS
                .add("min=" + min) //NON-NLS
                .add("max=" + max) //NON-NLS
                .add("units=" + units) //NON-NLS
                .toString();
    }

    public enum Units {
        CELSIUS {
            @Override
            double convert(final double value, final @NotNull Units units) {
                switch (units) {
                    case CELSIUS:
                        return value;
                    case KELVIN:
                        return value + 273.15;
                }
                throw new IllegalStateException();
            }
        }, KELVIN {
            @Override
            double convert(final double value, final @NotNull Units units) {
                switch (units) {
                    case CELSIUS:
                        return value - 273.15;
                    case KELVIN:
                        return value;
                }
                throw new IllegalStateException();
            }
        };

        abstract double convert(final double value, final @NotNull Units units);
    }
}
