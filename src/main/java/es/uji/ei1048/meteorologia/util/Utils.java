package es.uji.ei1048.meteorologia.util;

import es.uji.ei1048.meteorologia.service.NotFoundException;
import javafx.application.Platform;
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
        property.addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue) {
                toggleGroup.getToggles().stream()
                        .filter(it -> it.getProperties().get(propertyName) == newValue)
                        .findFirst()
                        .ifPresent(toggleGroup::selectToggle);
            }
        });
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue) {
                property.setValue((T) Objects.requireNonNull(toggleGroup).getSelectedToggle().getProperties().get(propertyName));
            }
        });

        Platform.runLater(() -> property.setValue((T) Objects.requireNonNull(toggleGroup).getSelectedToggle().getProperties().get(propertyName)));
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
