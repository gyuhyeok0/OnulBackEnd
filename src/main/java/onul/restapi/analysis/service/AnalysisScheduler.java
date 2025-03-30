package onul.restapi.analysis.service;

import lombok.extern.slf4j.Slf4j;
import onul.restapi.analysis.entity.MemberLastLogin;
import onul.restapi.analysis.repository.MemberLastLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Component
public class AnalysisScheduler {

    private final MemberLastLoginRepository memberLastLoginRepository;
    private final AnalysisService analysisService;

    @Autowired
    @Qualifier("analysis")
    private Executor analysisExecutor;

    public AnalysisScheduler(MemberLastLoginRepository memberLastLoginRepository,
                             AnalysisService analysisService) {
        this.memberLastLoginRepository = memberLastLoginRepository;
        this.analysisService = analysisService;
    }

    // 매일 새벽 12시
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Tokyo")
    public void runDailyAnalysis() {

        LocalDate yesterday = LocalDate.now().minusDays(1); // 어제 날짜
        log.info("▶ Daily analysis started for date: {}", yesterday);

        List<MemberLastLogin> logins = memberLastLoginRepository.findByLastLoginDate(yesterday);

        LocalDate today = LocalDate.now();

        for (MemberLastLogin login : logins) {
            String memberId = login.getMember().getMemberId();

            CompletableFuture.runAsync(() -> analysisService.updateVolumeStatistics(memberId, today), analysisExecutor)
                    .thenRunAsync(() -> analysisService.updateWeightAndDietStatistics(memberId, today), analysisExecutor)
                    .thenRunAsync(() -> analysisService.updateMuscleFatigue(memberId, today), analysisExecutor)
                    .exceptionally(ex -> {
                        log.error("❌ Error in analysis for member {}: {}", memberId, ex.getMessage());
                        return null;
                    });
        }
    }
}
