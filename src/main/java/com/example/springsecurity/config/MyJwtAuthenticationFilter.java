package com.example.springsecurity.config;

import com.example.springsecurity.service.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyJwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try{
        String jwtToken = extractJwtFromRequest(request);
    if(StringUtils.hasText(jwtToken) && jwtUtil.validateToken(jwtToken)) {
        UserDetails userDetails = new User(jwtUtil.getUsernameFromToken(jwtToken), "", jwtUtil.getRolesToken(jwtToken));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, "", jwtUtil.getRolesToken(jwtToken));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    } else {
        System.out.println("Can not set security context ");
    }} catch (ExpiredJwtException e) {
        request.setAttribute("exception", e);
    } catch (BadCredentialsException e) {
        request.setAttribute("exception", e);
    }
        filterChain.doFilter(request, response);
    }

    public String extractJwtFromRequest (HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
