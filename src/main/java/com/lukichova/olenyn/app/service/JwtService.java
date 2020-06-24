package com.lukichova.olenyn.app.service;

import com.lukichova.olenyn.app.DB.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtService {

    public static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String token="";


    public String getToken() {
        return this.token;

    }
    public boolean isJwsValid(String jws) throws ExpiredJwtException {
        try{
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(jws);
        }catch(JwtException e){
            return false;
        }
        return true;
    }
    public static String generateToken(final User user) {

        return Jwts.builder()
                .setSubject(user.getLogin())
                .signWith(SECRET_KEY)
                .claim("role", user.getRole())
                .compact();

    }


    public static String getUsernameFromToken(String jwt) {
        return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(jwt)
            .getBody()
            .getSubject();
    }


}
