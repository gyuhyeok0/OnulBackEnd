package onul.restapi.analysis.service;

import lombok.extern.slf4j.Slf4j;
import onul.restapi.analysis.entity.MemberLastLogin;
import onul.restapi.analysis.repository.MemberLastLoginRepository;
import onul.restapi.common.ReadinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AnalysisScheduler {

    private final MemberLastLoginRepository memberLastLoginRepository;
    private final AnalysisService analysisService;
    private final ReadinessService readinessService;

    public AnalysisScheduler(MemberLastLoginRepository memberLastLoginRepository, AnalysisService analysisService, ReadinessService readinessService) {
        this.memberLastLoginRepository = memberLastLoginRepository;
        this.analysisService = analysisService;
        this.readinessService = readinessService;
    }

    @Autowired
    @Qualifier("analysis")
    private Executor analysisExecutor;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;  // RedisTemplate 주입


    // 매일 자정 실행
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Tokyo")
    @Transactional
    public void runDailyAnalysis() {
        String lockKey = "dailyAnalysisLock";

        if (redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 10, TimeUnit.MINUTES)) {
            try {
                readinessService.markReadinessDown(); // 트래픽 차단
                LocalDate today = LocalDate.now();
                LocalDate yesterday = today.minusDays(1);

                log.info("▶ Daily analysis started for date: {}", yesterday);

                List<MemberLastLogin> logins = memberLastLoginRepository.findByLastLoginDate(yesterday);
                List<CompletableFuture<Void>> futures = new ArrayList<>();

                for (MemberLastLogin login : logins) {
                    String memberId = login.getMember().getMemberId();

                    CompletableFuture<Void> task = CompletableFuture
                            .runAsync(() -> analysisService.updateVolumeStatistics(memberId, today), analysisExecutor)
                            .thenRunAsync(() -> analysisService.updateWeightAndDietStatistics(memberId, today), analysisExecutor)
                            .thenRunAsync(() -> analysisService.updateMuscleFatigue(memberId, today), analysisExecutor)
                            .exceptionally(ex -> {
                                log.error("❌ Error in analysis for member {}: {}", memberId, ex.getMessage());
                                return null;
                            });

                    futures.add(task);
                }

                // 모든 작업 완료까지 대기
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
                log.info("✅ Daily analysis completed.");

            } catch (Exception e) {
                log.error("❌ Error during analysis: {}", e.getMessage(), e);
            } finally {
                redisTemplate.delete(lockKey);
                readinessService.markReadinessUp(); // 트래픽 다시 받기
            }
        } else {
            log.info("▶ Another instance is already running the analysis task.");
        }
    }
}

