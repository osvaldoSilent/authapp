
// JwtUtil.java
package com.example.authservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "secretkeyosvaldevopsproyectssacvoigatestingasddffsecretkeyosvaldevopsproyectssacvoigatestingasddffsecretkeyosvaldevopsproyectssacvoigatestingasddffsecretkeyosvaldevopsproyectssacvoigatestingasddffsecretkeyosvaldevopsproyectssacvoigatestingasddffsecretkeyosvaldevopsproyectssacvoigatestingasddff";
    private final long EXPIRATION_TIME = 864_000_00; // 1 día en milisegundos

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
