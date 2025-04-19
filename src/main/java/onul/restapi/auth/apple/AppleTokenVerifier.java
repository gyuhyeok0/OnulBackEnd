package onul.restapi.auth.apple;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URL;

@Component
public class AppleTokenVerifier {

    private static final String ISSUER = "https://appleid.apple.com";
    private static final String APPLE_KEYS_URL = "https://appleid.apple.com/auth/keys";

    private final AppleProperties appleProperties;
    private ConfigurableJWTProcessor<SecurityContext> jwtProcessor;

    public AppleTokenVerifier(AppleProperties appleProperties) {
        this.appleProperties = appleProperties;
    }

    @PostConstruct
    public void init() {
        try {
            jwtProcessor = new DefaultJWTProcessor<>();
            JWKSource<SecurityContext> keySource =
                    new RemoteJWKSet<>(new URL(APPLE_KEYS_URL));

            // ✅ Apple uses RS256, not ES256!
            JWSKeySelector<SecurityContext> keySelector =
                    new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, keySource);

            jwtProcessor.setJWSKeySelector(keySelector);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize AppleTokenVerifier", e);
        }
    }

    public String verifyAndExtractSub(String identityToken) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(identityToken);

        jwtProcessor.process(signedJWT, null);

        String issuer = signedJWT.getJWTClaimsSet().getIssuer();
        String audience = signedJWT.getJWTClaimsSet().getAudience().get(0);

        if (!ISSUER.equals(issuer)) {
            throw new IllegalArgumentException("Invalid issuer: " + issuer);
        }

        if (!appleProperties.getClientId().equals(audience)) {
            throw new IllegalArgumentException("Invalid audience: " + audience);
        }

        return signedJWT.getJWTClaimsSet().getSubject();
    }
}
