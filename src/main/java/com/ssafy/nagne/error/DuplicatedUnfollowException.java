package com.ssafy.nagne.error;

public class DuplicatedUnfollowException extends RuntimeException {

    public DuplicatedUnfollowException() {
    }

    public DuplicatedUnfollowException(String message) {
        super(message);
    }

    public DuplicatedUnfollowException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedUnfollowException(Throwable cause) {
        super(cause);
    }
}
