package com.gethighlow.highlowandroid.Exceptions;

public class InvalidRefreshToken extends Exception {
    public InvalidRefreshToken(String errorMessage) {
        super(errorMessage);
    }
}
