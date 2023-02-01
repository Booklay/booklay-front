package com.nhnacademy.booklay.booklayfront.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.auth.UsernamePasswordAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 기본 설정
 *
 * @author 조현진, 양승아
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper mapper;
    private final UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.formLogin()
            .loginProcessingUrl("/login")
            .usernameParameter("memberId")
            .passwordParameter("password")
            .loginPage("/members/login")
            .and()
            .logout().logoutUrl("/members/logout")
            .deleteCookies("SESSION_ID")
            .invalidateHttpSession(true)
            .logoutSuccessUrl("/");

        http.authorizeRequests()
            .anyRequest()
            .permitAll();

        http.csrf()
            .disable();

        http.authenticationProvider(usernamePasswordAuthenticationProvider);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
        return web -> web.ignoring()
                         .antMatchers("/resources/**", "/static/**", "/webjars/**", "/img/**");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
