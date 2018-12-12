package es.uji.ei1048.meteorologia.model.converter;

import es.uji.ei1048.meteorologia.model.City;
import javafx.util.StringConverter;
import org.jetbrains.annotations.NotNull;

public final class CityStringConverter extends StringConverter<@NotNull City> {
    private static final @NotNull StringConverter<@NotNull City> INSTANCE = new CityStringConverter();

    private CityStringConverter() {
    }

    public static @NotNull StringConverter<@NotNull City> getInstance() {
        return INSTANCE;
    }

    @Override
    public @NotNull String toString(final @NotNull City object) {
        return object.getName();
    }

    @Override
    public @NotNull City fromString(final @NotNull String string) {
        throw new UnsupportedOperationException("Conversion from String to City is not yet supported!");
    }
}
