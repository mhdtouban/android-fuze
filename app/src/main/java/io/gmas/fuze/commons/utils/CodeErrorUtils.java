package io.gmas.fuze.commons.utils;

public class CodeErrorUtils {

    public static boolean isUnauthorized(int code) {
        return code == Error.Unauthorized.code;
    }

    public static boolean isBadRequest(int code) {
        return code == Error.BadRequest.code;
    }

    public static boolean isForbidden(int code) {
        return code == Error.Forbidden.code;
    }

    public static boolean isNotFound(int code) {
        return code == Error.NotFound.code;
    }

    public static boolean isBadGateway(int code) {
        return code == Error.BadGateway.code;
    }

    public static boolean isInternalServerError(int code) {
        return code == Error.InternalServerError.code;
    }

    private enum Error {

        Unauthorized(401),
        BadRequest(400),
        Forbidden(403),
        NotFound(404),
        BadGateway(503),
        InternalServerError(500);

        private final int code;

        Error(int code) {
            this.code = code;
        }
    }

}
