package com.spiritcoder.musalalogistics.commons.exception;

public class MusalaLogisticsException extends RuntimeException {

    public MusalaLogisticsException(String message) {
        super(message);
    }

    public MusalaLogisticsException(String message, Throwable cause) {
        super(message, cause);
    }
}