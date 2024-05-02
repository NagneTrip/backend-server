package com.ssafy.nagne.hashtag;

import java.util.Arrays;
import java.util.List;
import org.springframework.core.convert.converter.Converter;

public class HashTagConverter implements Converter<String, List<String>> {

    @Override
    public List<String> convert(String source) {
        return Arrays.stream(source.split("\\s"))
                .toList();
    }
}
