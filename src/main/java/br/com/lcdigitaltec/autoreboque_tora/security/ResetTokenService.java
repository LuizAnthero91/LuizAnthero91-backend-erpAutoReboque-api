package br.com.lcdigitaltec.autoreboque_tora.security;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

@Component
public class ResetTokenService {

    private final SecureRandom secureRamdom =
            new SecureRandom();

    public String gerarToken() {
        byte[] bytes = new byte[32];

        secureRamdom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

    }
    public String gerarHash(String token) {

        try {
            MessageDigest digest =
                MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of()
                    .formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "SHA-256 não disponivel",
                    e
            );
        }
    }
}
