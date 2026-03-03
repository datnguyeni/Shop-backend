package com.datnguyeni.shop_backend.service;

import com.datnguyeni.shop_backend.entity.Role;
import com.datnguyeni.shop_backend.entity.User;
import com.datnguyeni.shop_backend.mapper.UserMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SIGNER_KEY;

    public String generateToken(User user) throws JOSEException {

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        // do not let vulnerable information here
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getFirstname())
                .issuer("shop-backend")
                .issueTime(new Date())
                .expirationTime(Date.from(
                        Instant.now().plus(1, ChronoUnit.HOURS)
                ))
                .claim("roles", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

        return jwsObject.serialize();
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

    //"scope": "ROLE_USER", "ROLE_ADMIN"
    private String buildScope(User user){
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return "";
        }

        return user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.joining(" "));
    }

}
