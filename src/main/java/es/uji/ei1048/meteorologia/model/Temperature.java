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
    private final @NotNull Temperature.Units unit;

    public Temperature(
            final double current,
            final double min,
            final double max,
            final @NotNull Temperature.Units unit
    ) {
        this.current = current;
        this.min = min;
        this.max = max;
        this.unit = unit;
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

    public @NotNull Units getUnit() {
        return unit;
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Temperature)) return false;
        final @NotNull Temperature that = (Temperature) obj;
        return Double.compare(that.current, current) == 0 &&
                Double.compare(that.min, min) == 0 &&
                Double.compare(that.max, max) == 0 &&
                unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(current, min, max, unit);
    }

    @Override
    public @NotNull String toString() {
        return new StringJoiner(", ", Temperature.class.getSimpleName() + "[", "]")
                .add("current=" + current) //NON-NLS
                .add("min=" + min) //NON-NLS
                .add("max=" + max) //NON-NLS
                .add("unit=" + unit) //NON-NLS
                .toString();
    }

    @SuppressWarnings("ReturnOfThis")
    public @NotNull Temperature convertTo(final @NotNull Units newUnit) {
        return unit == newUnit ? this : new Temperature(
                unit.convert(current, newUnit),
                unit.convert(min, newUnit),
                unit.convert(max, newUnit),
                newUnit
        );
    }

    public enum Units {
        CELSIUS {
            @Override
            public double convert(final double value, final @NotNull Units unit) {
                switch (unit) {
                    case CELSIUS:
                        return value;
                    case KELVIN:
                        return value + 273.15;
                }
                return super.convert(value, unit);
            }
        }, KELVIN {
            @Override
            public double convert(final double value, final @NotNull Units unit) {
                switch (unit) {
                    case CELSIUS:
                        return value - 273.15;
                    case KELVIN:
                        return value;
                }
                return super.convert(value, unit);
            }
        };

        public double convert(final double value, @NonNls final @NotNull Units unit) {
            throw new IllegalArgumentException("Unrecognized unit " + unit);
        }
    }
}
