package com.example.userservice.Config;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Component
@WebFilter
public class JwtAuthenticationFilter  {}
/*
    @Value("${keycloak.urls.auth}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    private static final String JWK_URL = "/protocol/openid-connect/certs";  // JWK URL for Keycloak


        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                DecodedJWT decodedJWT = JWT.decode(token);

                try {
                    // Fetch the JWK Set from Keycloak's JWK URL
                    URL jwkUrl = new URL(authServerUrl + "/realms/" + realm + JWK_URL);
                    InputStreamReader reader = new InputStreamReader(jwkUrl.openStream());
                    JSONObject jwkSet = new JSONObject(new JSONTokener(reader));

                    // Extract the correct JWK from the JWK Set based on the "kid" field in the JWT header
                    JSONArray keys = jwkSet.getJSONArray("keys");
                    JSONObject jwk = null;
                    for (int i = 0; i < keys.length(); i++) {
                        JSONObject key = keys.getJSONObject(i);
                        if (key.getString("kid").equals(decodedJWT.getKeyId())) {
                            jwk = key;
                            break;
                        }
                    }

                    if (jwk != null) {
                        // Extract the public key from the JWK
                        RSAPublicKey rsaPublicKey = getRSAPublicKey(jwk);

                        // Create JWT verifier with the public key
                        Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, null);

                        // Verify the JWT token
                        JWTVerifier verifier = JWT.require(algorithm)
                                .withIssuer(authServerUrl + "/realms/" + realm)
                                .withAudience("backend-api")
                                .build();

                        verifier.verify(decodedJWT);  // Verifies the token
                    } else {
                        throw new ServletException("JWK not found for key ID: " + decodedJWT.getKeyId());
                    }
                } catch (Exception e) {
                    throw new ServletException("JWT verification failed", e);
                }
            }

            filterChain.doFilter(request, response);  // Continue filter chain
        }

        // Method to convert JWK to RSAPublicKey

    private RSAPublicKey getRSAPublicKey(JSONObject jwk) {
        String modulus = jwk.getString("n");
        String exponent = jwk.getString("e");

        // Decode the modulus and exponent (Base64Url decoding)
        byte[] modulusBytes = Base64.getUrlDecoder().decode(modulus);
        byte[] exponentBytes = Base64.getUrlDecoder().decode(exponent);

        // Convert bytes to BigInteger
        BigInteger modulusBI = new BigInteger(1, modulusBytes);
        BigInteger exponentBI = new BigInteger(1, exponentBytes);

        // Create RSAPublicKeySpec (modulus, exponent)
        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulusBI, exponentBI);

        try {
            // Now create the RSAPublicKey
            RSAPublicKey rsaPublicKey = (RSAPublicKey) java.security.KeyFactory.getInstance("RSA").generatePublic(spec);
            return rsaPublicKey;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create RSA public key", e);
        }
    }
}

    //implements Converter<Jwt, AbstractAuthenticationToken>
/*
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;


    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()).collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }

    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        return jwt.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        Collection<String> allRoles = new ArrayList<>();
        Collection<String> resourceRoles;
        Collection<String> realmRoles;

        if (resourceAccess != null && resourceAccess.get("account") != null) {
            Map<String, Object> account = (Map<String, Object>) resourceAccess.get("account");
            if (account.containsKey("roles")) {
                resourceRoles = (Collection<String>) account.get("roles");
                allRoles.addAll(resourceRoles);
            }
        }

        if (realmAccess != null && realmAccess.containsKey("roles")) {
            realmRoles = (Collection<String>) realmAccess.get("roles");
            allRoles.addAll(realmRoles);
        }

        if (allRoles.isEmpty() || !Objects.equals(resourceId, jwt.getClaim("azp"))) {
            return Set.of();
        }

        return allRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }*/



