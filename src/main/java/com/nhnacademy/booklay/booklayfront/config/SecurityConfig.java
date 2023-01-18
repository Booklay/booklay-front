package com.nhnacademy.booklay.booklayfront.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.auth.AuthenticationServerProxy;
import com.nhnacademy.booklay.booklayfront.auth.UsernamePasswordAuthenticationProvider;
import com.nhnacademy.booklay.booklayfront.filter.AuthenticationFilter;
import com.nhnacademy.booklay.booklayfront.filter.InitialAuthenticationFilter;
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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

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
        http.formLogin().disable();

        http.authorizeRequests()
                .anyRequest()
                .permitAll();

        http.csrf()
                .disable();

        http.addFilterAt(getInitialAuthenticationFilter(), BasicAuthenticationFilter.class);

        http.authenticationProvider(usernamePasswordAuthenticationProvider);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
        return web -> web.ignoring()
                .antMatchers("/resources/**", "/static/**", "/webjars/**");
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

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter =
                new AuthenticationFilter(authenticationManager(null), mapper);

        authenticationFilter.setFilterProcessesUrl("/members/doLogin");

        return authenticationFilter;
    }

    private InitialAuthenticationFilter getInitialAuthenticationFilter() throws Exception {
        return new InitialAuthenticationFilter(authenticationManager(null));
    }

}
