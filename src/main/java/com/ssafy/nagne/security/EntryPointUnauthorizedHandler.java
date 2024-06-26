package com.ssafy.nagne.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {

    private static final String _401 = """
            {
                "success" : false,
                "response" : null,
                "error" : {
                    "message" : "Unauthorized",
                    "status" : 401
                }
            }
            """;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("content-type", "application/json");
        response.getWriter().write(_401);
        response.getWriter().flush();
        response.getWriter().close();
    }

}