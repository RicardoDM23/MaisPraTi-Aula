package com.example.SpringAula69.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenProvider {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    //parserBuilder
    public Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
    
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt()
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userName = extractUsername(token);
        return(userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    //boolean
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    
    private boolean extractExpiration(String token) {
        extractClaim(token, Claims::getExpiration);
    }
    
}
