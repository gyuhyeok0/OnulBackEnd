package onul.restapi.auth.config;

import io.lettuce.core.cluster.ClusterClientOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.Collections;

@Configuration
public class RedisConfig {

    @Value("${REDIS_HOST}")
    private String redisHost;

    @Value("${REDIS_PORT}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        if (redisHost == null || redisHost.isEmpty()) {
            throw new IllegalArgumentException("REDIS_HOST environment variable is not set.");
        }

        // 1. 클러스터 구성 (포트 6380 명시)
        RedisClusterConfiguration clusterConfig =
                new RedisClusterConfiguration(Collections.singletonList(redisHost + ":" + redisPort));

        // 2. 클러스터 옵션 (리디렉션 시에도 SSL 유지)
        ClusterClientOptions clusterOptions = ClusterClientOptions.builder()
                .validateClusterNodeMembership(false) // 서버리스는 이 설정 필요
                .build();

        // 3. Lettuce 클라이언트 구성
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .useSsl() // 필수
                .and()
                .commandTimeout(Duration.ofMillis(60000))
                .shutdownTimeout(Duration.ofSeconds(2))
                .clientOptions(clusterOptions)
                .build();

        // 4. 최종 RedisConnectionFactory 반환
        LettuceConnectionFactory factory = new LettuceConnectionFactory(clusterConfig, clientConfig);
        factory.setShareNativeConnection(false); // 클러스터 안전성 확보
        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }
}
