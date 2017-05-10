package io.gmas.fuze.commons.services.deserializers.parsers.impl;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

public abstract class BaseParser <Type> {

    public abstract @NonNull Type parseJson(@NonNull JsonObject jo) throws Exception;

}
