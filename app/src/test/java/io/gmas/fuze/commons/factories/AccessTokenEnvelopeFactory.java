package io.gmas.fuze.commons.factories;

import io.gmas.fuze.commons.services.apiresponses.AccessTokenEnvelope;

public final class AccessTokenEnvelopeFactory {
    private AccessTokenEnvelopeFactory() {}

    private static AccessTokenEnvelope envelope() {
        return AccessTokenEnvelope.builder()
                .accessToken("accessToken")
                .user(UserFactory.creator())
                .build();
    }

    public static AccessTokenEnvelope creator() {
        return envelope();
    }
}
