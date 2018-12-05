package es.uji.ei1048.meteorologia.api;

import org.apache.http.StatusLine;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum ApiUtils {
    ;

    private static final int NOT_FOUND = 404;

    @Contract(" -> fail")
    ApiUtils() {
        throw new UnsupportedOperationException("This is aUtility class no new instances should be created.");
    }

    /**
     * @param status The status of a response from a service
     * @throws NotFoundException If the status code was 404 indicating that the given city was not found
     */
    public static void checkStatus(final @NotNull StatusLine status) {
        if (status.getStatusCode() == NOT_FOUND) throw new NotFoundException(status.getReasonPhrase());
    }
}
