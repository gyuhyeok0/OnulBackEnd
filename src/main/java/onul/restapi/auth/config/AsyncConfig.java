package onul.restapi.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    private int getCpuCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * 하루 1회 실행
     * AI 요청 전에 필요한 DB 정보 로드
     * CPU 점유율 낮고, 실행 간단함
     */
    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int cpuCores = getCpuCores();

        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(cpuCores >= 4 ? 3 : 1);
        executor.setQueueCapacity(10000);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.initialize();
        return executor;
    }

    /**
     * AI 요청 실행
     * 앱 실행 시 하루 1~2회 작동, CPU 사용 중간
     * 순간적으로 몰릴 가능성 낮음
     */
    @Bean(name = "aiExecutor")
    public Executor aiExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int cpuCores = getCpuCores();

        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(cpuCores >= 4 ? 3 : 1);
        executor.setQueueCapacity(10000);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("AiExecutor-");
        executor.initialize();
        return executor;
    }

    /**
     * 자정마다 실행되는 자동 분석 작업
     * 동시에 여러 사용자 분석이 돌아갈 수 있어 여유 필요
     */
    @Bean(name = "analysis")
    public Executor analysisExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int cpuCores = getCpuCores();

        executor.setCorePoolSize(cpuCores >= 4 ? 2 : 1);
        executor.setMaxPoolSize(cpuCores >= 4 ? 4 : 2);
        executor.setQueueCapacity(100000);
        executor.setKeepAliveSeconds(60); // 자정 작업이라 조금 오래 살아도 됨
        executor.setThreadNamePrefix("AnalysisExecutor-");
        executor.initialize();
        return executor;
    }

    /**
     * 회원 탈퇴 요청
     * 시간 오래 걸려도 되고, 사용자 수 적음
     */
    @Bean(name = "withdrawExecutor")
    public Executor withdrawExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(10000);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("WithdrawExecutor-");
        executor.initialize();
        return executor;
    }
}
