package com.ssafy.nagne.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ApiRequestResponseBodyMethodProcessorConfig implements WebMvcConfigurer {

    private final List<HttpMessageConverter<?>> converters;

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(new ApiRequestResponseBodyMethodProcessor(converters));
    }
}
