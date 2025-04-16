package onul.restapi.analysis.controller;

import lombok.RequiredArgsConstructor;
import onul.restapi.analysis.service.AnalysisScheduler;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/scheduler")
public class ManualSchedulerController {

    private final AnalysisScheduler analysisScheduler;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String SECRET_KEY = "3a9d-f03c-7bf2-secure-9b17";
    private static final String DAILY_LIMIT_KEY_PREFIX = "manualRunCount:";

    @PostMapping("/run-daily-analysis")
    public ResponseEntity<String> runDailyAnalysisNow(@RequestHeader("X-SECRET-KEY") String key) {
        if (!SECRET_KEY.equals(key)) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        String today = LocalDate.now(ZoneId.of("America/Los_Angeles")).toString();
        String redisKey = DAILY_LIMIT_KEY_PREFIX + today;

        // 실행 횟수 가져오기
        Integer count = redisTemplate.opsForValue().get(redisKey) != null
                ? Integer.parseInt(redisTemplate.opsForValue().get(redisKey))
                : 0;

        if (count >= 2) {
            return ResponseEntity.status(429).body("❌ 실행 횟수 초과 (하루 최대 2회)");
        }

        // 실행 횟수 +1
        redisTemplate.opsForValue().increment(redisKey);
        // 키가 처음 만들어졌다면, 오늘 자정까지 TTL 설정 (24시간 유지)
        if (count == 0) {
            redisTemplate.expire(redisKey, 1, TimeUnit.DAYS);
        }

        analysisScheduler.runDailyAnalysis();
        return ResponseEntity.ok("✅ Daily analysis executed manually.");
    }
}
