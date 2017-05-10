/*
 * Copyright 2017 Kickstarter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ***
 *
 * Original: https://github.com/kickstarter/android-oss/blob/master/app/src/main/java/com/kickstarter/libs/ApiEndpoint.java
 * Modifications: Some modifiers and annotations have been added by Guillaume Mas.
 */

package io.gmas.fuze.commons;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.gmas.fuze.commons.utils.Secrets;

public enum ApiEndpoint {
    PRODUCTION("Production", Secrets.Api.Endpoint.PRODUCTION),
    DEVELOPMENT("Development", Secrets.Api.Endpoint.DEVELOPMENT),
    CUSTOM("Custom", null);

    private String name;
    private String url;

    ApiEndpoint(final @NonNull String name, final @Nullable String url) {
        this.name = name;
        this.url = url;
    }

    public @NonNull String url() {
        return url;
    }

    @Override public String toString() {
        return name;
    }

    public static ApiEndpoint from(final @NonNull String url) {
        for (final ApiEndpoint value : values()) {
            if (value.url != null && value.url.equals(url)) {
                return value;
            }
        }
        final ApiEndpoint endpoint = CUSTOM;
        endpoint.url = url;
        return endpoint;
    }
}
