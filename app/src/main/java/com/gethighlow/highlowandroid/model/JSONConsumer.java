package com.gethighlow.highlowandroid.model;

import org.json.JSONException;

@FunctionalInterface
public interface JSONConsumer<T> {
    void accept(T t) throws JSONException;
}
