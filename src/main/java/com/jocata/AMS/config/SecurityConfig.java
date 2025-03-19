//package com.jocata.AMS.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//
//
//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig {
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        return http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authorizeExchange(auth -> auth
//                                .pathMatchers("/api/**").hasAnyRole("ADMIN", "USER")
//                                .pathMatchers("/api/admin/**").hasRole("ADMIN")
//                                .anyExchange().authenticated())
//                .httpBasic(Customizer.withDefaults())
//                .build();
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
//        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
//    }
//
//}
