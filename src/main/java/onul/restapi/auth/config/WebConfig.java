package onul.restapi.auth.config;

import onul.restapi.auth.filter.HeaderFilter;
import onul.restapi.auth.interceptor.JwtTokenInterceptor;
import onul.restapi.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private final TokenUtils tokenUtils;

    @Autowired
    public WebConfig(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // CORS 설정
        String serverIp = System.getenv("SERVER_IP");

        registry.addMapping("/**")  // 모든 경로에 대해 CORS를 허용
                .allowedOrigins(serverIp, "http://localhost:8081")  // 환경 변수로 설정된 IP 사용
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드
                .allowedHeaders("*")  // 허용할 헤더
                .allowCredentials(true);  // 자격 증명 허용 (예: 쿠키)
    }

    @Bean
    public FilterRegistrationBean<HeaderFilter> getFilterRegistrationBean() {
        FilterRegistrationBean<HeaderFilter> registrationBean = new FilterRegistrationBean<>(createHeaderFilter());
        registrationBean.setOrder(Integer.MIN_VALUE);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    public HeaderFilter createHeaderFilter() {
        return new HeaderFilter();
    }

    @Bean
    public JwtTokenInterceptor jwtTokenInterceptor() {
        return new JwtTokenInterceptor(tokenUtils);  // TokenUtils 주입
    }
}
