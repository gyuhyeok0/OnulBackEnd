package onul.restapi.analysis.service;

import lombok.extern.slf4j.Slf4j;
import onul.restapi.analysis.entity.MemberLastLogin;
import onul.restapi.analysis.repository.MemberLastLoginRepository;
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
    private final Executor asyncExecutor;

    public AnalysisScheduler(MemberLastLoginRepository memberLastLoginRepository,
                             AnalysisService analysisService,
                             @Qualifier("asyncExecutor") Executor asyncExecutor) {
        this.memberLastLoginRepository = memberLastLoginRepository;
        this.analysisService = analysisService;
        this.asyncExecutor = asyncExecutor;
    }

    // 매일 새벽 1시 (서버 시간이 KST 기준이면 UTC로 16시)
    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Tokyo")
    public void runDailyAnalysis() {
        LocalDate today = LocalDate.now();
        log.info("▶ Daily analysis started for date: {}", today);

        List<MemberLastLogin> logins = memberLastLoginRepository.findByLastLoginDate(today);
        log.info("▶ Found {} users for analysis.", logins.size());

        for (MemberLastLogin login : logins) {
            String memberId = login.getMember().getMemberId();

            CompletableFuture.runAsync(() -> analysisService.updateVolumeStatistics(memberId, today), asyncExecutor)
                    .thenRunAsync(() -> analysisService.updateWeightAndDietStatistics(memberId, today), asyncExecutor)
                    .thenRunAsync(() -> analysisService.updateMuscleFatigue(memberId, today), asyncExecutor)
                    .exceptionally(ex -> {
                        log.error("❌ Error in analysis for member {}: {}", memberId, ex.getMessage());
                        return null;
                    });
        }
    }
}
