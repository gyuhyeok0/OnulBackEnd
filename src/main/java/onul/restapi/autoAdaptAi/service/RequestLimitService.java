package onul.restapi.autoAdaptAi.service;

import onul.restapi.util.TokenUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class RequestLimitService {

    private final StringRedisTemplate redisTemplate;
    private final TokenUtils tokenUtils;

    private static final int MAX_REQUESTS = 2;  // 1분 동안 최대 2번 요청 가능
    private static final int TIME_WINDOW_SECONDS = 60; // 1분 (60초)

    public RequestLimitService(StringRedisTemplate redisTemplate, TokenUtils tokenUtils) {
        this.redisTemplate = redisTemplate;
        this.tokenUtils = tokenUtils;
    }

    public boolean isRequestAllowed(String authHeader) {
        String userToken = tokenUtils.splitHeader(authHeader); // "Bearer " 제거 후 토큰만 가져오기
        if (userToken == null || !tokenUtils.isValidToken(userToken)) {
            return false;  // 유효하지 않은 토큰은 차단
        }

        // 토큰에서 memberId 추출 (유저 ID 기반으로 제한)
        String memberId = tokenUtils.getClaimsFromToken(userToken).get("memberId", String.class);
        if (memberId == null) {
            return false; // 토큰에서 memberId를 추출할 수 없으면 차단
        }

        String countKey = "aiRequest:count:" + memberId;  // 요청 횟수 저장
        String timeKey = "aiRequest:time:" + memberId;    // 마지막 요청 시간 저장

        // 현재 요청 횟수 가져오기
        String requestCountStr = redisTemplate.opsForValue().get(countKey);
        int requestCount = (requestCountStr != null) ? Integer.parseInt(requestCountStr) : 0;

        // 마지막 요청 시간 가져오기
        String lastRequestTimeStr = redisTemplate.opsForValue().get(timeKey);
        long lastRequestTime = (lastRequestTimeStr != null) ? Long.parseLong(lastRequestTimeStr) : 0;

        long currentTime = Instant.now().getEpochSecond(); // 현재 시간 (초 단위)

        if (lastRequestTime == 0 || (currentTime - lastRequestTime) >= TIME_WINDOW_SECONDS) {
            // 8분이 지났다면 요청 횟수 초기화
            redisTemplate.opsForValue().set(countKey, "1", TIME_WINDOW_SECONDS, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(timeKey, String.valueOf(currentTime), TIME_WINDOW_SECONDS, TimeUnit.SECONDS);
            return true; // 첫 번째 요청 허용
        }

        if (requestCount < MAX_REQUESTS) {
            // 요청 횟수 증가
            redisTemplate.opsForValue().increment(countKey);
            redisTemplate.opsForValue().set(timeKey, String.valueOf(currentTime), TIME_WINDOW_SECONDS, TimeUnit.SECONDS);
            return true; // 2번째 요청 허용
        }

        return false; // 3번째 요청부터 차단
    }
}
