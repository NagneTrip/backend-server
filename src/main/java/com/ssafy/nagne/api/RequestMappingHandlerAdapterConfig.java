package com.ssafy.nagne.api;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

@Configuration
@RequiredArgsConstructor
public class RequestMappingHandlerAdapterConfig {

    private final RequestMappingHandlerAdapter handlerAdapter;

    @PostConstruct
    public void prioritizeCustomArgumentMethodHandlers() {
        ApiRequestResponseBodyMethodProcessor newHandler = new ApiRequestResponseBodyMethodProcessor(
                handlerAdapter.getMessageConverters());

        addArgumentResolver(newHandler);
        addReturnValueHandler(newHandler);
    }

    private void addArgumentResolver(ApiRequestResponseBodyMethodProcessor newHandler) {
        List<HandlerMethodArgumentResolver> newArgumentResolvers = new ArrayList<>();
        for (HandlerMethodArgumentResolver argumentResolver : handlerAdapter.getArgumentResolvers()) {
            if (argumentResolver instanceof RequestResponseBodyMethodProcessor) {
                newArgumentResolvers.add(newHandler);
                continue;
            }

            newArgumentResolvers.add(argumentResolver);
        }

        handlerAdapter.setArgumentResolvers(null);
        handlerAdapter.setArgumentResolvers(newArgumentResolvers);
    }

    private void addReturnValueHandler(ApiRequestResponseBodyMethodProcessor newHandler) {
        List<HandlerMethodReturnValueHandler> newReturnValueHandlers = new ArrayList<>();
        for (HandlerMethodReturnValueHandler returnValueHandler : handlerAdapter.getReturnValueHandlers()) {
            if (returnValueHandler instanceof RequestResponseBodyMethodProcessor) {
                newReturnValueHandlers.add(newHandler);
                continue;
            }

            newReturnValueHandlers.add(returnValueHandler);
        }

        handlerAdapter.setReturnValueHandlers(null);
        handlerAdapter.setReturnValueHandlers(newReturnValueHandlers);
    }
}
