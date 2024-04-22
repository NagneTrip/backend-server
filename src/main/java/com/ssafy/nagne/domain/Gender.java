package com.ssafy.nagne.domain;

import java.util.Arrays;

public enum Gender {

    MAN("MAN"),
    WOMAN("WOMAN");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static Gender of(String name) {
        return Arrays.stream(values())
                .filter(gender -> gender.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Bad Gender"));
    }
}
