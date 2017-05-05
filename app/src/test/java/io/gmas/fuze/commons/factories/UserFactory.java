package io.gmas.fuze.commons.factories;

import io.gmas.fuze.commons.models.User;

public final class UserFactory {
    private UserFactory() {}

    public static User user() {
        return User.builder()
                .id(1234567890L)
                .firstName("android")
                .lastName("android")
                .email("android@yummypets.com")
                .birthDate("27/02/1990")
                .gender("M")
                .country(119)
                .build();
    }

    public static User creator() {
        return user();
    }

}
