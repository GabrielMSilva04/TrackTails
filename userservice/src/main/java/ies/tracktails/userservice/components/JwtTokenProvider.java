package ies.tracktails.userservice.components;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.RSAKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final KeyPair keyPair;
    private final long jwtExpirationMs;

    public JwtTokenProvider(@Value("${jwt.privateKeyPath}") String privateKeyPath,
                            @Value("${jwt.publicKeyPath}") String publicKeyPath,
                            @Value("${jwt.expiration}") long jwtExpirationMs) throws Exception {
        this.keyPair = loadKeys(privateKeyPath, publicKeyPath);
        this.jwtExpirationMs = jwtExpirationMs;
    }

    private KeyPair loadKeys(String privateKeyPath, String publicKeyPath) throws Exception {
        PrivateKey privateKey = getPrivateKey(privateKeyPath);
        PublicKey publicKey = getPublicKey(publicKeyPath);
        return new KeyPair(publicKey, privateKey);
    }

    
    private PrivateKey getPrivateKey(String filePath) throws Exception {
        String privateKeyPEM = new String(Files.readAllBytes(Paths.get(filePath)));
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "")
                                     .replace("-----END PRIVATE KEY-----", "")
                                     .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
    
    private PublicKey getPublicKey(String filePath) throws Exception {
        String publicKeyPEM = new String(Files.readAllBytes(Paths.get(filePath)));
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "")
                                   .replace("-----END PUBLIC KEY-----", "")
                                   .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public String generateToken(Long userId) {
        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
            .setHeaderParam("kid", "tracktails-key-id")  // Aqui adicionamos o 'kid'
            .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
            .compact();    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(keyPair.getPublic())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Token inválido ou expirado.", e);
        }
    }

    public Long getUserIdFromToken(String token) {
        String subject = Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.parseLong(subject);
    }

    public Map<String, Object> getJwk() throws IOException {
        RSAKey jwk = new RSAKey.Builder((java.security.interfaces.RSAPublicKey) keyPair.getPublic())
                .keyID("tracktails-key-id")
                .algorithm(JWSAlgorithm.RS256)
                .build();
        return jwk.toJSONObject();
    }
}
