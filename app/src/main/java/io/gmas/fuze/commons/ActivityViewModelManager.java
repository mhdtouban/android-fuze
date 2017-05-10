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
 * Original: https://github.com/kickstarter/android-oss/blob/master/app/src/main/java/com/kickstarter/libs/ActivityViewModelManager.java
 * Modifications: Some modifiers and annotations have been added by Guillaume Mas.
 */

package io.gmas.fuze.commons;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import io.gmas.fuze.FZApplication;
import io.gmas.fuze.commons.session.Session;
import io.gmas.fuze.commons.utils.BundleUtils;

public class ActivityViewModelManager {

    private static final String VIEW_MODEL_ID_KEY = "view_model_id";
    private static final String VIEW_MODEL_STATE_KEY = "view_model_state";

    private static final ActivityViewModelManager instance = new ActivityViewModelManager();
    private Map<String, ActivityViewModel> viewModels = new HashMap<>();

    public static @NonNull ActivityViewModelManager getInstance() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T extends ActivityViewModel> T fetch(final @NonNull Context context, final @NonNull Class<T> viewModelClass,
                                                 final @Nullable Bundle savedInstanceState) {
        final String id = fetchId(savedInstanceState);
        ActivityViewModel activityViewModel = viewModels.get(id);

        if (activityViewModel == null) {
            activityViewModel = create(context, viewModelClass, savedInstanceState, id);
        }

        return (T) activityViewModel;
    }

    public void destroy(final @NonNull ActivityViewModel activityViewModel) {
        activityViewModel.onDestroy();

        final Iterator<Map.Entry<String, ActivityViewModel>> iterator = viewModels.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, ActivityViewModel> entry = iterator.next();
            if (activityViewModel.equals(entry.getValue())) {
                iterator.remove();
            }
        }
    }

    public void save(final @NonNull ActivityViewModel activityViewModel, final @NonNull Bundle envelope) {
        envelope.putString(VIEW_MODEL_ID_KEY, findIdForViewModel(activityViewModel));

        final Bundle state = new Bundle();
        envelope.putBundle(VIEW_MODEL_STATE_KEY, state);
    }

    private <T extends ActivityViewModel> ActivityViewModel create(final @NonNull Context context, final @NonNull Class<T> viewModelClass,
                                                                   final @Nullable Bundle savedInstanceState, final @NonNull String id) {

        final FZApplication application = (FZApplication) context.getApplicationContext();
        final Session session = application.component().session();
        final ActivityViewModel activityViewModel;

        try {
            final Constructor constructor = viewModelClass.getConstructor(Session.class);
            activityViewModel = (ActivityViewModel) constructor.newInstance(session);

            // Need to catch these exceptions separately, otherwise the compiler turns them into `ReflectiveOperationException`.
            // That exception is only available in API19+
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        } catch (InvocationTargetException exception) {
            throw new RuntimeException(exception);
        } catch (InstantiationException exception) {
            throw new RuntimeException(exception);
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }

        viewModels.put(id, activityViewModel);

        activityViewModel.onCreate(context, BundleUtils.maybeGetBundle(savedInstanceState, VIEW_MODEL_STATE_KEY));

        return activityViewModel;
    }

    private String fetchId(final @Nullable Bundle savedInstanceState) {
        return savedInstanceState != null ?
                savedInstanceState.getString(VIEW_MODEL_ID_KEY) :
                UUID.randomUUID().toString();
    }

    private String findIdForViewModel(final @NonNull ActivityViewModel activityViewModel) {
        for (final Map.Entry<String, ActivityViewModel> entry : viewModels.entrySet()) {
            if (activityViewModel.equals(entry.getValue())) {
                return entry.getKey();
            }
        }

        throw new RuntimeException("Cannot find view model in map!");
    }
}
