package com.ssafy.nagne.domain;

import java.util.Arrays;

public enum Tier {

    DIAMOND,
    PLATINUM,
    GOLD,
    SILVER,
    BRONZE,
    UNRANKED;

    public static Tier of(String name) {
        return Arrays.stream(values())
                .filter(gender -> gender.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Bad Gender"));
    }
}
