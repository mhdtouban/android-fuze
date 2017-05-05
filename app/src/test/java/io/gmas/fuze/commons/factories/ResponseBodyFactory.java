package io.gmas.fuze.commons.factories;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

public final class ResponseBodyFactory {
    private ResponseBodyFactory() {}

    private static ResponseBody responseBody() {
        return new ResponseBody() {
            @Override public MediaType contentType() {
                return null;
            }

            @Override public long contentLength() {
                return 0;
            }

            @Override public BufferedSource source() {
                return null;
            }
        };
    }

    public static ResponseBody creator() {
        return responseBody();
    }

}
