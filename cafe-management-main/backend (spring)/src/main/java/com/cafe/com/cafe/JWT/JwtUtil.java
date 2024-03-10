package com.cafe.com.cafe.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtUtil {
    private String secret = "aanxniee";

    // retrieves username from token
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    // retrieves expiration date of the token
    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        // setSigningKey sets the signing key to verify any JWS
        // parseClaimsJws parses the specific compact serialize JWS string
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    // checks if token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        String jws = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // token is valid for 10 hours
                .signWith(SignatureAlgorithm.HS256, secret).compact(); // signature section of the token
        return jws;
    }

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // if the username is valid and the token is not expired, it is valid
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
