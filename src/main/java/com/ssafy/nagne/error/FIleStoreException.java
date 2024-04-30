package com.ssafy.nagne.error;

public class FIleStoreException extends RuntimeException {

    public FIleStoreException() {
    }

    public FIleStoreException(String message) {
        super(message);
    }

    public FIleStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public FIleStoreException(Throwable cause) {
        super(cause);
    }
}
