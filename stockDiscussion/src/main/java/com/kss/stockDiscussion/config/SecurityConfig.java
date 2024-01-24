package com.kss.stockDiscussion.config;

import com.kss.stockDiscussion.config.jwt.JwtAuthenticationFilter;
import com.kss.stockDiscussion.config.jwt.JwtAuthorizationFilter;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
@Configuration
public class SecurityConfig {
    private static final String[] userFilter = {"/user/**"};
    private static final String[] managerFilter = {"/manager/**"};
    private static final String[] adminFilter = {"/admin/**"};
    private final String loginURL = "/api/auth/log-in";
    private final UserRepository userRepository;
    private final CorsConfig corsConfig;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new MyCustomDsl())
                .and()
                .authorizeRequests(a -> {
                    try {
                        a.antMatchers(userFilter).access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                                .antMatchers(managerFilter).access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                                .antMatchers(adminFilter).access("hasRole('ROLE_ADMIN')")
                                .anyRequest().permitAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        return http.build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager);
            jwtAuthenticationFilter.setFilterProcessesUrl(loginURL);
            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(jwtAuthenticationFilter)
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
        }
    }

}
