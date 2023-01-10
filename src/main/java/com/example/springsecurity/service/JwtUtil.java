package com.example.springsecurity.service;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.util.*;

@Service
public class JwtUtil {

    private String secret;
    private int expirationInMs;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }
    @Value("${jwt.expirationDateInMs}")
    public void setExpiration(int expirationInMs) {
        this.expirationInMs = expirationInMs;
    }

    public String generateToken(UserDetails userDetails) {
        Map <String, Object> claims = new HashMap<>();
        Collection <? extends GrantedAuthority> role = userDetails.getAuthorities();
        if(role.contains( new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            claims.put("isAdmin", true);
        }
        if(role.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            claims.put("isUser", true);
        }
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String doGenerateToken(Map <String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationInMs)).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public  boolean validateToken (String authToken) {
        try{
            Jws <Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        } catch (ExpiredJwtException e) {
            throw e;
        }
    }

    public List <SimpleGrantedAuthority> getRolesToken (String authToken) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken).getBody();
        List<SimpleGrantedAuthority> role = new ArrayList<>();
        Boolean isAdmin = claims.get("isAdmin", Boolean.class);
        Boolean isUser = claims.get("isUser", Boolean.class);
        if(isAdmin != null && isAdmin) {
            role = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if(isUser != null && isUser) {
            role = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return role;
    }

    public String getUsernameFromToken (String authToken) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken).getBody();
        return  claims.getSubject();
    }
}
