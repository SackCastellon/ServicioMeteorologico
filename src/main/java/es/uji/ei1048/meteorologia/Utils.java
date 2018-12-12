package es.uji.ei1048.meteorologia;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.scene.control.ToggleGroup;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public enum Utils {
    ;

    @SuppressWarnings("unchecked")
    public static <T> void bindToToggleGroup(
            final @NotNull Property<@NotNull T> property,
            final @NotNull ToggleGroup toggleGroup,
            final @NotNull String propertyName) {
        property.bind(Bindings.createObjectBinding(
                () -> (T) Objects.requireNonNull(toggleGroup).getSelectedToggle().getProperties().get(propertyName),
                toggleGroup.selectedToggleProperty()
        ));
    }
}
