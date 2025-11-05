package com.sampleDataBase.security;


import com.sampleDataBase.auth.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
//@RequiredArgsConstructor
public class JWTConfiguration {
//    private final Properties properties;
    private String secretKey = "";
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15; // 15 min
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7;


    public JWTConfiguration() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public String generateAccessToken(Users users) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("roles",users.getRoles());
        return Jwts.builder()
                .claims(claims)
                .subject(users.getUserName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getKey())
                .compact();
    }
    public String generateRefreshToken(Users user) {
        return Jwts.builder()
                .subject(user.getUserName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getKey())
                .compact();
    }


//            | Line                            | Purpose                                              | Why                                          |
//            | ------------------------------- | ---------------------------------------------------- | -------------------------------------------- |
//            | `claims(claims)`                | Add extra metadata (e.g., roles, user id, org, etc.) | These become part of JWT payload             |
//            | `.subject(users.getUserName())` | Sets the “subject” (the main identifier of the user) | Used later to identify who owns the token    |
//            | `.issuedAt(new Date(...))`      | Timestamp when the token was created                 | Helps with token validity checks             |
//            | `.expiration(new Date(...))`    | When the token expires                               | Enforces logout/time-based validity          |
//            | `.signWith(getKey())`           | Signs the JWT using your secret key                  | Ensures authenticity — token can’t be forged |
//            | `.compact()`                    | Builds and encodes the token into a single string    | Final Base64URL-encoded JWT                  |


    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


}