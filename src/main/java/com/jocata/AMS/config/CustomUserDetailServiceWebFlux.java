//package com.jocata.AMS.config;
//
//import com.jocata.AMS.dao.UserDao;
//import com.jocata.AMS.forms.UserForm;
//import com.jocata.AMS.service.UserService;
//import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Mono;
//
//@Component
//public class CustomUserDetailService implements ReactiveUserDetailsService {
//    private final UserDao userDao;
//
//    public CustomUserDetailService(UserDao userDao) {
//        this.userDao = userDao;
//
//    }
//
//    @Override
//    public Mono<UserDetails> findByUsername(String email) {
//        return Mono.justOrEmpty(userDao.getUserByEmail(email))
//                .doOnNext(user -> {
//                    System.out.println("✅ User found: " + user.getEmail());
//                    System.out.println("🎭 Role: " + user.getRole());
//                    System.out.println("🔑 Stored Password: " + user.getPasswordHash());
//                })
//                .map(user -> User
//                        .withUsername(user.getEmail())
//                        .password("{noop}"+ user.getPasswordHash())
//                        .authorities("ROLE_" +user.getRole().name())
//                        .build())
//                .doOnSuccess(userDetails -> System.out.println("🎯 Successfully authenticated: " + userDetails.getUsername()))
//                .doOnError(error -> System.out.println("❌ Error during authentication: " + error.getMessage()))
//                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")));
//    }
//    }
//
