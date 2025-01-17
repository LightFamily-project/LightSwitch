package com.lightswitch.security.config

import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .authorizeHttpRequests { auth ->
                auth
                    .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.REQUEST).permitAll()
                    .anyRequest().authenticated()	// 어떠한 요청이라도 인증필요
            }
            // 로그인 설정
            .formLogin { form ->
                form
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", true)
                    .permitAll()
            }
            // 로그아웃 설정
            .logout(Customizer.withDefaults())
        return http.build()
    }

}
