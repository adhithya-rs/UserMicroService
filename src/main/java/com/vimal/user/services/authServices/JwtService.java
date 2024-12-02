package com.vimal.user.services.authServices;

import com.vimal.user.customEceptions.GenerateTokenException;
import com.vimal.user.enums.UserType;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    private final Key SECRET_KEY;
    private static final long REGISTRATION_EXPIRATION_TIME = 600000; // 10 mins=600000
    private static final long RETAILER_EXPIRATION_TIME = 172800000; // 1 hour=3600000
    private static final long USER_EXPIRATION_TIME = 604800000; // 7 Days=604800000

    public JwtService(@Value("${jwt.secretKey}") String secretKey) {
        this.SECRET_KEY = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");;
    }

    public String generateRegistrationToken(String email, UserType type){
        try {
            Date now = new Date(); // Capture the current time
            Date expirationDate = new Date(System.currentTimeMillis() + (REGISTRATION_EXPIRATION_TIME));

            return Jwts.builder()
                    .setSubject(email)
                    .claim("roles", type.name())
                    .setIssuedAt(now)
                    .setId(UUID.randomUUID().toString())
                    .setExpiration(expirationDate)
                    .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                    .compact();
        }catch (Exception e) {
            // Handle other exceptions
            throw new GenerateTokenException("Error generating registration token");
        }
    }

    public void validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            if (claimsJws.getBody().getExpiration().before(new Date())) {
                throw new ExpiredJwtException(null, null, "Token has expired");
            }
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException("Malformed token");
        }
    }


    public String getSubjectFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody().getSubject();
    }
    public String getUserTypeFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody().get("roles", String.class);
    }

    public String generateSigninToken(String hashedId, UserType type){
        try {
            Date now = new Date(); // Capture the current time
            Date expirationDate = new Date(System.currentTimeMillis() + (type==UserType.CUSTOMER? USER_EXPIRATION_TIME:RETAILER_EXPIRATION_TIME));

            return Jwts.builder()
                    .setSubject(hashedId)
                    .claim("roles", type.name())
                    .setIssuedAt(now)
                    .setId(UUID.randomUUID().toString())
                    .setExpiration(expirationDate)
                    .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                    .compact();
        }catch (Exception e) {
            // Handle other exceptions
            throw new GenerateTokenException("Error generating SignIn token");
        }
    }
}
