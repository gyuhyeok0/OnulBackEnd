package onul.restapi.util;


import onul.restapi.member.dto.MemberDTO;
import io.jsonwebtoken.*;
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

    private static String jwtSecretKey;
    private static Long tokenValidateTime;


    // 여기서 토큰을 생성한다
    // member 로 받은 데이터를 token 으로 발행
    public static String generateJwtToken(MemberDTO member) {
//        System.out.println("TokenUtils 토큰 생성 중"+member);
        Date expireTime = new Date(System.currentTimeMillis()+tokenValidateTime);

        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(member))
                .setSubject(member.getMemberId())
                .signWith(SignatureAlgorithm.HS256,createSignature())
                .setExpiration(expireTime);

        return builder.compact();
    }

    private static Key createSignature() {
        byte[] secretBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);
        return new SecretKeySpec(secretBytes,SignatureAlgorithm.HS256.getJcaName());
    }

    private static Map<String, Object> createClaims(MemberDTO member) {
        Map<String,Object> claims = new HashMap<>();

        claims.put("memberId", member.getMemberId());
        return claims;
    }


    private static Map<String,Object> createHeader(){
        Map<String,Object> header = new HashMap<>();

        header.put("type","jwt");
        header.put("alg","HS256");
        header.put("date",System.currentTimeMillis());

        return header;
    }

    @Value("${jwt.key}")
    public void setJwtSecretKey(String jwtSecretKey){
        TokenUtils.jwtSecretKey = jwtSecretKey;
    }

    @Value("${jwt.time}")
    public void setTokenValidateTime(Long tokenValidateTime){
        TokenUtils.tokenValidateTime =tokenValidateTime;
    }

    public static String splitHeader(String header){
        if(!header.equals("")){
            return header.split(" ")[1];
        }else{
            return null;
        }
    }

    public static boolean isValidToken(String token){

        try{
            Claims claims = getClaimsFromToken(token);
            return true;
        }catch (ExpiredJwtException e){
            e.printStackTrace();
            return false;
        }catch (JwtException e){
            e.printStackTrace();
            return false;
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }


    // 암호화한 토큰 디코딩
    // yml 파일에 저장된 jwtkey 를 이용하여 디코딩
    public static Claims getClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecretKey))
                .parseClaimsJws(token).getBody();
    }



}
