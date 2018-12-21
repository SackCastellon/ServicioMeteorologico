package es.uji.ei1048.meteorologia.util;

import es.uji.ei1048.meteorologia.service.NotFoundException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.scene.control.ToggleGroup;
import org.apache.http.StatusLine;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("This is a Utility class no new instances should be created.");
    }

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

    /**
     * @param status The status of a response from a service
     * @throws NotFoundException If the status code was 404 indicating that the given city was not found
     */
    public static void checkStatus(final @NotNull StatusLine status) {
        final int code = status.getStatusCode();
        if (code >= 400 && code < 500) throw new NotFoundException(status.getReasonPhrase());
    }
}
