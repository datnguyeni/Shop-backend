package com.datnguyeni.shop_backend.service;

import com.datnguyeni.shop_backend.entity.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SIGNER_KEY;

    public String generateToken(User user) {

        try {

            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("shop-backend")
                    .issueTime(new Date())
                    .expirationTime(Date.from(
                            Instant.now().plus(1, ChronoUnit.HOURS)
                    ))
                    .claim("roles", buildScope(user))
                    .build();

            JWSObject jwsObject = new JWSObject(
                    jwsHeader,
                    new Payload(jwtClaimsSet.toJSONObject())
            );

            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            return jwsObject.serialize();

        } catch (JOSEException e) {
            throw new RuntimeException("Error while generating JWT token", e);
        }
    }

    public boolean validateToken(String token){

        try{

            JWSObject jwsObject = JWSObject.parse(token);
            // 1. Verify Signature
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            // header + current payload
            if(!jwsObject.verify(verifier)){
                return false;
            }
            // 2. Parse Claims
            JWTClaimsSet claims = JWTClaimsSet.parse(jwsObject.getPayload().toString());
            Date expirationTime = claims.getExpirationTime();

            // 3. Check if 'exp' exists and is in the future
            return expirationTime != null && expirationTime.after(new Date());

        } catch (Exception e) {
            return false;
        }

    }


    //"scope": "USER", "ADMIN"
    private String buildScope(User user){
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return "";
        }

        return user.getRoles().stream()
                .map(role -> "ROLE_" + role.getRoleName().toUpperCase().trim())
                .collect(Collectors.joining(" "));
    }

}
