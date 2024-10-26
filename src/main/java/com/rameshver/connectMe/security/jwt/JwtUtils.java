package com.rameshver.connectMe.security.jwt;

import com.rameshver.connectMe.security.user.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class JwtUtils {

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.expirationInMils}")
    private int expirationTime;

    public String generateTokenForUser(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // Convert authorities (roles) to a list of strings
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());  // Change to .toList() if Java 16+

        // Build and sign the JWT
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())  // Set subject (user's email)
            .claim("id", userPrincipal.getId())    // Add custom claims (id)
            .claim("roles", roles)                 // Add roles to claims
                .setIssuedAt(new Date())               // Set token issue time
            .setExpiration(new Date((new Date()).getTime() + expirationTime))  // Set expiration
            .signWith(key(), SignatureAlgorithm.HS256)  // Sign the token with HS256
            .compact();  // Compact the JWT into a string
}

// Generate the signing key from the Base64-encoded secret
private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));  // Decode the secret
}

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SecurityException |
                 IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }
}
