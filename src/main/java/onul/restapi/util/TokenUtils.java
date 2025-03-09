package onul.restapi.util;

import onul.restapi.member.dto.MemberDTO;
import io.jsonwebtoken.*;
import onul.restapi.member.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtils {

    private String jwtSecretKey;
    private long accessTokenExpirationTime;
    private long refreshTokenExpirationTime;

    private final RedisService redisService;

    @Autowired
    public TokenUtils(RedisService redisService) {
        this.redisService = redisService;
    }

    @Value("${jwt.access-token-time}")
    public void setAccessTokenExpirationTime(long accessTokenExpirationTime) {
        this.accessTokenExpirationTime = accessTokenExpirationTime;
    }

    @Value("${jwt.refresh-token-time}")
    public void setRefreshTokenExpirationTime(long refreshTokenExpirationTime) {
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    // JWT 토큰 생성
    public String generateJwtToken(MemberDTO member) {
        Date expireTime = new Date(System.currentTimeMillis() + accessTokenExpirationTime);

        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(member))
                .setSubject(member.getMemberId())
                .signWith(SignatureAlgorithm.HS256, createSignature())
                .setExpiration(expireTime);

        return builder.compact();
    }

    // JWT Refresh Token 생성
    public String generateRefreshToken(MemberDTO member) {
        String existingToken = redisService.getRefreshToken(member.getMemberId());

        // 기존 토큰이 유효한 경우
        if (existingToken != null && isValidToken(existingToken)) {
            return existingToken; // 기존 토큰 반환
        }

        // 새로운 리프레시 토큰 생성
        Date expireTime = new Date(System.currentTimeMillis() + refreshTokenExpirationTime); // 만료 시간 설정

        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(member))
                .setSubject(member.getMemberId())
                .signWith(SignatureAlgorithm.HS256, createSignature())
                .setExpiration(expireTime);

        String refreshToken = builder.compact();

        // TTL 없이 Redis에 토큰 저장
        redisService.saveRefreshToken(member.getMemberId(), refreshToken); // TTL 없이 저장

        return refreshToken; // 새로운 토큰 반환
    }


    private Key createSignature() {
        byte[] secretBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);
        return new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private Map<String, Object> createClaims(MemberDTO member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", member.getMemberId());
        return claims;
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("type", "JWT");
        header.put("alg", "HS256");
        return header;
    }

    @Value("${jwt.key}")
    public void setJwtSecretKey(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    public boolean isValidToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (JwtException | NullPointerException e) {
            return false;
        }
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecretKey))
                .parseClaimsJws(token).getBody();
    }

    public String splitHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // "Bearer " 이후의 토큰 부분만 추출
        } else {
            return null;
        }
    }

    public boolean isRefreshTokenExpired(String refreshToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecretKey))
                    .parseClaimsJws(refreshToken)
                    .getBody();

            Date expiration = claims.getExpiration(); // 만료 시간 가져오기

            return expiration.before(new Date()); // 현재 시간과 비교

        } catch (JwtException e) {
            return true; // 유효하지 않으면 만료된 것으로 간주
        }
    }

}

