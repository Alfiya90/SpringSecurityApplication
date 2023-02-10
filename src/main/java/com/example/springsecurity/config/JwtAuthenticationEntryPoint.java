package com.example.springsecurity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      /*  response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Authorization");*/
        Exception exception = (Exception) request.getAttribute("exception");
        String message;
        if(exception != null) {
            if(authException.getCause() != null) {
                message = exception.getCause() + exception.getMessage();
            }  else {
                message = exception.getMessage();
            }
            byte []  body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message));
            response.getOutputStream().write(body);


        } else {
            if(authException.getCause() != null) {
                message = authException.getCause() + authException.getMessage();
            }  else {
                message = authException.getMessage();
            }
            byte []  body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message));
            response.getOutputStream().write(body);
        }


    }
}
