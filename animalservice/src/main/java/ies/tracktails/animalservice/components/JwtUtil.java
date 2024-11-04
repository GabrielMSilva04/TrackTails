package ies.tracktails.animalservice.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JwtUtil {

    private PublicKey publicKey;

    @Value("${jwt.public.key.path}")
    private String publicKeyPath;

    @PostConstruct
    private void loadPublicKey() {
        try {
            System.out.println("Loading public key from path: " + publicKeyPath); // Log para verificar o caminho
            String publicKeyPEM = new String(Files.readAllBytes(Paths.get(publicKeyPath)))
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
            this.publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error loading the public key: " + e.getMessage(), e);
        }
    }

    public String extractUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token has expired", e);
        } catch (SignatureException e) {
            throw new RuntimeException("Invalid JWT signature", e);
        } catch (JwtException e) {
            throw new RuntimeException("Failed to parse JWT token", e);
        }
    }
}