package com.example.basicback.user.config;

import com.example.basicback.user.jwt.JwtAuthenticationFilter;
import com.example.basicback.user.jwt.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/login", "/api/register", "/api/register/validation").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/login", "/api/register", "/api/register/validation").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/login", "/api/register", "/api/register/validation").permitAll()
                        .requestMatchers(HttpMethod.GET, "/reports", "/reports/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/reports").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/reports/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/reports/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/summarize").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/disaster-message").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/disaster-message").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/missing-message").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/missing-message").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // 모든 origin 패턴 허용
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization")); // JWT를 위한 Authorization 헤더 노출

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}