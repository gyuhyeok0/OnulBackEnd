package onul.restapi.auth.config;

import onul.restapi.auth.filter.CustomAuthenticationFilter;
import onul.restapi.auth.filter.JwtAuthorizationFilter;
import onul.restapi.auth.handler.CustomAuthFailUserHandler;
import onul.restapi.auth.handler.CustomAuthSuccessHandler;
import onul.restapi.auth.handler.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // 정적 자원에 대한 인증된 사용자의 접근 가능 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web -> web.ignoring().requestMatchers("/css/**","/js/**","/images/**","/lib/**","/memberimgs/**"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class)
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form->form.disable())
                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(basic->basic.disable());

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(){
        return  new ProviderManager(customAuthenticationProvider());
    }
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(){
        return new CustomAuthenticationProvider();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(){
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/auth/login");
        customAuthenticationFilter.setAuthenticationSuccessHandler(customAuthSuccessHandler());
        customAuthenticationFilter.setAuthenticationFailureHandler(customAuthFailUserHandler());
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    private AuthenticationFailureHandler customAuthFailUserHandler() {
        return new CustomAuthFailUserHandler();
    }

    @Bean
    public CustomAuthSuccessHandler customAuthSuccessHandler(){
        return new CustomAuthSuccessHandler();
    }

    public JwtAuthorizationFilter jwtAuthorizationFilter() {

        return new JwtAuthorizationFilter(authenticationManager());
    }


}
