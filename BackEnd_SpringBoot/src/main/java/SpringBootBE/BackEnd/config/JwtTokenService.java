package SpringBootBE.BackEnd.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
public class JwtTokenService {

    private static final Base64.Encoder BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding();

    private final String jwtSecret;
    private final long expirationMs;

    public JwtTokenService(
            @Value("${app.jwt.secret:change-me-to-a-secure-jwt-secret}") String jwtSecret,
            @Value("${app.jwt.expiration-ms:86400000}") long expirationMs) {
        this.jwtSecret = jwtSecret;
        this.expirationMs = expirationMs;
    }

    public TokenData generateToken(Integer userId, String email, String role, String access) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusMillis(Math.max(expirationMs, 60_000L));

        String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payloadJson = "{" +
                "\"sub\":\"" + escape(email) + "\"," +
                "\"userId\":" + userId + "," +
                "\"role\":\"" + escape(role) + "\"," +
                "\"access\":\"" + escape(access) + "\"," +
                "\"iat\":" + issuedAt.getEpochSecond() + "," +
                "\"exp\":" + expiresAt.getEpochSecond() +
                "}";

        String unsignedToken = encode(headerJson) + "." + encode(payloadJson);
        String signature = sign(unsignedToken);

        return new TokenData(unsignedToken + "." + signature, expiresAt.toEpochMilli());
    }

    private String encode(String value) {
        return BASE64_URL_ENCODER.encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String escape(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return BASE64_URL_ENCODER.encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Không thể ký JWT token.", exception);
        }
    }

    public record TokenData(String token, long expiresAt) {
    }
}
