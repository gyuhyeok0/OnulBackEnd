package onul.restapi.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync  // ✅ @Async 기능 활성화
public class AsyncConfig {

    //
    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);  // ✅ 동시에 실행할 스레드 수 (3명만 동시 실행)
        executor.setMaxPoolSize(2);   // ✅ 최대 스레드 수도 3로 고정 (더 이상 늘어나지 않음)
        executor.setQueueCapacity(1000); //
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "aiExecutor")
    public Executor aiExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);  // ✅ AI 요청 전용 스레드 개수 (2개)
        executor.setMaxPoolSize(2);   // ✅ 최대 스레드 수도 2개로 고정
        executor.setQueueCapacity(1000); // ✅ 초과된 요청은 최대 1000개까지 대기 가능
        executor.setThreadNamePrefix("AiExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "analysis")
    public Executor analysis() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);  // ✅ 1인당 3스레드가 동작되는데 4로 해도 충분한가?
        executor.setMaxPoolSize(3);   // ✅
        executor.setQueueCapacity(1000); // ✅ 대기열 1000
        executor.setThreadNamePrefix("Analysis-");
        executor.initialize();
        return executor;
    }

}
