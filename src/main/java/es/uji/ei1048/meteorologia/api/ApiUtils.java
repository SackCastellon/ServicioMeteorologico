package es.uji.ei1048.meteorologia.api;

import org.apache.http.StatusLine;
import org.jetbrains.annotations.NotNull;

public final class ApiUtils {

    private ApiUtils() {
        throw new UnsupportedOperationException("This is a Utility class no new instances should be created.");
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
