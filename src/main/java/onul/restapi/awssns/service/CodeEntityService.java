package onul.restapi.awssns.service;

import onul.restapi.awssns.entity.CodeEntity;
import onul.restapi.awssns.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CodeEntityService {

    private final CodeRepository codeRepository;
    private final RedisTemplate<String, String> redisTemplate;  // RedisTemplate 주입

    @Autowired
    public CodeEntityService(CodeRepository codeRepository, RedisTemplate<String, String> redisTemplate) {
        this.codeRepository = codeRepository;
        this.redisTemplate = redisTemplate;
    }

    // 매일 자정에 1주일 지난 데이터 삭제
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void deleteExpiredCodes() {
        // Redis로 분산 잠금 적용
        String lockKey = "deleteExpiredCodesLock";  // 잠금 키
        if (redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 10, TimeUnit.MINUTES)) {
            try {
                long oneWeekAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000; // 일주일 전의 타임스탬프

                // 일주일이 지난 인증 코드 삭제
                List<CodeEntity> expiredCodes = codeRepository.findByExpiryTimeBefore(oneWeekAgo);
                codeRepository.deleteAll(expiredCodes);
            } catch (Exception e) {
                // 예외 처리
                e.printStackTrace();
            } finally {
                // 작업 완료 후 잠금 해제
                redisTemplate.delete("deleteExpiredCodesLock");
            }
        } else {
            // 이미 다른 인스턴스가 작업을 수행 중일 때
            System.out.println("▶ Another instance is already running the deleteExpiredCodes task.");
        }
    }
}
