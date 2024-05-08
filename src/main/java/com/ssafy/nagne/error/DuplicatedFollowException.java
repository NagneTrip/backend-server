package com.ssafy.nagne.error;

public class DuplicatedFollowException extends RuntimeException {

    public DuplicatedFollowException() {
    }

    public DuplicatedFollowException(String message) {
        super(message);
    }

    public DuplicatedFollowException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedFollowException(Throwable cause) {
        super(cause);
    }
}
