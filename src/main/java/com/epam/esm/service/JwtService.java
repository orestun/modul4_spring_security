package com.epam.esm.service;

import com.epam.esm.exception.ItemNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class JwtService {

    private static final int JWT_TOKEN_LIFE_TIME_30_MIN = 1000 * 60 * 30;
    private final String SECRET_KEY;

    public JwtService(@Value("${jwt.secret-key}") String secretKey) {
        SECRET_KEY = secretKey;
    }

    public String generateToken(String username){
        Map<String, Object> claims= new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String username){
        Date issuetDate = this.getCurrentDateTimeInMilliseconds();
        return Jwts.
                builder().
                addClaims(claims).
                setSubject(username).
                setIssuedAt(issuetDate).
                setExpiration(getExpirationDate(issuetDate)).
                signWith(decodeSecretKeyToBase64(SECRET_KEY), SignatureAlgorithm.HS256).
                compact();
    }

    private Date getCurrentDateTimeInMilliseconds(){
        return new Date(System.currentTimeMillis());
    }

    private Date getExpirationDate(Date issuedDate){
        return new Date(issuedDate.getTime() + JwtService.JWT_TOKEN_LIFE_TIME_30_MIN);
    }

    private Key decodeSecretKeyToBase64(String secretKey){
        byte[] keyInBase64 = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyInBase64);
    }

    private Claims extractAllClaims(String token) throws Throwable {
        Claims claims;
        try{
            claims = Jwts.
                    parserBuilder().
                    setSigningKey(decodeSecretKeyToBase64(SECRET_KEY)).
                    build().parseClaimsJws(token).
                    getBody();
        }catch (ExpiredJwtException e){
            throw new ItemNotFoundException("Your jwt token is expired, refresh it", 40409L);
        }
        return Optional.ofNullable(claims).
                orElseThrow((Supplier<Exception>) () -> new ItemNotFoundException("Cells are not found", 40404L));
    }

    public String getUsername(String token) throws Throwable {
        Claims c = extractAllClaims(token);
        return extractAllClaims(token).getSubject();
    }

    public Date getExpirationDate(String token) throws Throwable {
        return extractAllClaims(token).getExpiration();
    }

    private boolean isTokenExpired(String token) throws Throwable {
        Date currentDateTime = new Date(System.currentTimeMillis());
        return getExpirationDate(token).
                after(currentDateTime);
    }

    public boolean validateToken(String token, UserDetails userDetails) throws Throwable {
        String username = getUsername(token);
        return (username.equals(userDetails.getUsername()) &&
                isTokenExpired(token));
    }
}
