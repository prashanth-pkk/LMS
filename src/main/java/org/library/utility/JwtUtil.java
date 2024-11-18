package org.library.utility;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String username) {

        SecretKey signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Ensure expiration value is valid
   /*     if (expiration <= 0) {
            throw new IllegalArgumentException("Expiration time must be positive and greater than zero.");
        }*/

        // Validate secret key (ensure it is not empty or null)
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret key must not be null or empty.");
        }

        // Create the JWT token with the given username and expiration
        return Jwts.builder()
                .setSubject(username)  // Set the username as the subject of the token
                .setIssuedAt(new Date())  // Set the current date and time as the issued date
                .setExpiration(new Date(System.currentTimeMillis() + expiration))  // Set the expiration time
                .signWith(signingKey)  // Sign the token with the specified algorithm and secret
                .compact();  // Return the compact JWT string
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }
}
