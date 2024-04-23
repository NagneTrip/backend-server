package com.ssafy.nagne.page;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PageParameterHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String DEFAULT_LAST_INDEX_PARAMETER = "lastIndex";
    private static final String DEFAULT_SIZE_PARAMETER = "size";

    private static final long DEFAULT_LAST_INDEX = Long.MAX_VALUE;
    private static final int DEFAULT_SIZE = 10;

    private String offsetParameterName = DEFAULT_LAST_INDEX_PARAMETER;
    private String sizeParameterName = DEFAULT_SIZE_PARAMETER;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Pageable.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            MethodParameter methodParameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        String lastIndexString = webRequest.getParameter(offsetParameterName);
        String sizeString = webRequest.getParameter(sizeParameterName);

        long lastIndex = lastIndexString == null ? DEFAULT_LAST_INDEX : Long.parseLong(lastIndexString);
        int size = sizeString == null ? DEFAULT_SIZE : Integer.parseInt(sizeString);

        return new PageParameter(lastIndex, size);
    }

    public void setOffsetParameterName(String offsetParameterName) {
        this.offsetParameterName = offsetParameterName;
    }

    public void setSizeParameterName(String sizeParameterName) {
        this.sizeParameterName = sizeParameterName;
    }
}