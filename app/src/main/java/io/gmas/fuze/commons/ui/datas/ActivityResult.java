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
 * Original: https://github.com/kickstarter/android-oss/blob/master/app/src/main/java/com/kickstarter/ui/data/ActivityResult.java
 * Modifications: Some modifiers and annotations have been added by Guillaume Mas.
 */

package io.gmas.fuze.commons.ui.datas;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ActivityResult {
    public abstract int requestCode();
    public abstract int resultCode();
    public abstract @Nullable Intent intent();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder requestCode(int __);
        public abstract Builder resultCode(int __);
        public abstract Builder intent(Intent __);
        public abstract ActivityResult build();
    }

    public static @NonNull ActivityResult create(final int requestCode, final int resultCode, final @Nullable Intent intent) {
        return ActivityResult.builder()
                .requestCode(requestCode)
                .resultCode(resultCode)
                .intent(intent)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ActivityResult.Builder();
    }

    public abstract Builder toBuilder();

    public boolean isCanceled() {
        return resultCode() == Activity.RESULT_CANCELED;
    }

    public boolean isOk() {
        return resultCode() == Activity.RESULT_OK;
    }

    public boolean isRequestCode(final int v) {
        return requestCode() == v;
    }
}

