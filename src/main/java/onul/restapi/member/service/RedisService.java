package onul.restapi.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // Refresh Token 저장 (유효 기간 설정 가능)
    public void saveRefreshToken(String memberId, String refreshToken) {
        redisTemplate.opsForValue().set(memberId, refreshToken);
    }

    // Refresh Token 가져오기
    public String getRefreshToken(String memberId) {
        String token = redisTemplate.opsForValue().get(memberId);
        System.out.println("Fetching refresh token for memberId: " + memberId + ", Token: " + token);
        return token;
    }



    // Refresh Token 삭제 (비밀변호 변경시)
    public void deleteRefreshToken(String memberId) {

        System.out.println("삭제되었음");
        redisTemplate.delete(memberId);
    }

    // Refresh Token 유효성 검사
    public boolean isRefreshTokenValid(String memberId, String refreshToken) {
        String savedToken = getRefreshToken(memberId);
        return savedToken != null && savedToken.equals(refreshToken);
    }
}
