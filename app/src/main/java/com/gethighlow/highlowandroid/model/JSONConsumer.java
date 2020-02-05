package com.gethighlow.highlowandroid.model;

import org.json.JSONException;

import java.util.function.Consumer;

@FunctionalInterface
public interface JSONConsumer<T> {
    void accept(T t) throws JSONException;
}
