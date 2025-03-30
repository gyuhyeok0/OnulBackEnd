package onul.restapi.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync  // ✅ @Async 기능 활성화
public class AsyncConfig {

    // ai 요청전 필요한 db 데이터 가지고옴 1인당 cpu 40% 미만, ai 요청시에만 작동
    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);  // ✅ 동시에 실행할 스레드 수 (2명만 동시 실행)
        executor.setMaxPoolSize(3);   // ✅ 최대 스레드 수도 2로 고정 (더 이상 늘어나지 않음)
        executor.setQueueCapacity(100000); //
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.initialize();
        return executor;
    }

    // ai 요청 1인당 cpu 60% 앱 킬때 하루 1번, 하루 평균 1.3 회 작동. 1 인당 0.5 초정도 걸림
    @Bean(name = "aiExecutor")
    public Executor aiExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);  // ✅ AI 요청 전용 스레드 개수 (2개)
        executor.setMaxPoolSize(3);   // ✅ 최대 스레드 수도 2개로 고정
        executor.setQueueCapacity(100000); // ✅ 초과된 요청은 최대 1000개까지 대기 가능
        executor.setThreadNamePrefix("AiExecutor-");
        executor.initialize();
        return executor;
    }

    // am 12 시 자동 작동, 1인당 cpu 100% 이하로 작동 예상, 1 인당 0.5 초정도 걸림
    @Bean(name = "analysis")
    public Executor analysis() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);  // ✅ 1인당 2스레드 동작
        executor.setMaxPoolSize(4);   // ✅
        executor.setQueueCapacity(100000); // ✅ 대기열 1000
        executor.setThreadNamePrefix("Analysis-");
        executor.initialize();
        return executor;
    }

    // 회원 탈퇴 오래걸려도 상관없음
    @Bean(name = "withdrawExecutor")
    public Executor withdrawExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(100000);
        executor.setThreadNamePrefix("WithdrawExecutor-");
        executor.initialize();
        return executor;
    }

}
