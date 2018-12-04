package es.uji.ei1048.meteorologia.api;

import org.apache.http.StatusLine;

public final class ApiUtils {

    private ApiUtils() {
        throw new UnsupportedOperationException();
    }

    public static void checkStatus(StatusLine status) throws NotFoundException {
        if (status.getStatusCode() == 404) throw new NotFoundException(status.getReasonPhrase());
    }
}
