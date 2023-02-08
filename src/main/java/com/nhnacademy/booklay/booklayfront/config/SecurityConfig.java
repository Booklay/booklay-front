package com.nhnacademy.booklay.booklayfront.config;

import com.nhnacademy.booklay.booklayfront.auth.AuthenticationServerProxy;
import com.nhnacademy.booklay.booklayfront.auth.CustomAuthenticationProvider;
import com.nhnacademy.booklay.booklayfront.auth.filter.JwtAuthenticationFilter;
import com.nhnacademy.booklay.booklayfront.auth.handler.CustomLoginFailureHandler;
import com.nhnacademy.booklay.booklayfront.auth.handler.CustomLoginSuccessHandler;
import com.nhnacademy.booklay.booklayfront.auth.handler.OAuth2LoginSuccessHandler;
import com.nhnacademy.booklay.booklayfront.auth.jwt.TokenUtils;
import com.nhnacademy.booklay.booklayfront.event.MemberEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 기본 설정
 *
 * @author 조현진, 양승아
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final AuthenticationServerProxy proxy;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.formLogin()
            .loginPage("/member/login")
            .loginProcessingUrl("/login")
            .usernameParameter("memberId")
            .passwordParameter("password")
            .successHandler(new CustomLoginSuccessHandler(memberEventPublisher(null)))
            .failureHandler(customLoginFailureHandler())
            .and()
            .logout().logoutUrl("/member/logout")
            .deleteCookies("SESSION_ID")
            .invalidateHttpSession(true)
            .logoutSuccessUrl("/");

        http.authorizeRequests()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/admin/coupon-zone/**").hasRole("ADMIN")
            .anyRequest()
            .permitAll();

        http.csrf()
            .disable();

        http.oauth2Login(c -> c.clientRegistrationRepository(clientRegistrationRepository())
                               .successHandler(oAuth2LoginSuccessHandler(null, null))
                               .failureHandler(customLoginFailureHandler()));

        http.addFilterBefore(jwtAuthenticationFilter(null, null), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
        return web -> web.ignoring()
                         .antMatchers("/resources/**", "/static/**", "/webjars/**", "/img/**", "/css/**", "/js/**",
                                      "/scss/**", "/plugins/**", "/mail/**");
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

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(proxy, userDetailsService, passwordEncoder());
    }

    private ClientRegistration clientRegistration() {
        return CommonOAuth2Provider.GITHUB
                   .getBuilder("github")
                   .clientId("11750a6c662e8dd0135c")
                   .clientSecret("4066e502af0568041afa744a0205d803be24842f")
                   .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration clientRegistration = clientRegistration();
        return new InMemoryClientRegistrationRepository(clientRegistration);
    }

    @Bean
    public OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler(AuthenticationServerProxy proxy, MemberEventPublisher memberEventPublisher) {
        return new OAuth2LoginSuccessHandler(proxy, memberEventPublisher);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(TokenUtils tokenUtils, AuthenticationServerProxy proxy) {
        return new JwtAuthenticationFilter(tokenUtils, proxy);
    }

    @Bean
    public CustomLoginFailureHandler customLoginFailureHandler() {
        return new CustomLoginFailureHandler();
    }

    @Bean
    public MemberEventPublisher memberEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new MemberEventPublisher(applicationEventPublisher);
    }

}
