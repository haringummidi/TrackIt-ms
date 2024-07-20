package com.shellonfire.trackitms.config;

import com.shellonfire.trackitms.filter.JWTAuthenticationFilter;
import com.shellonfire.trackitms.service.CustomUserDetailsService;
import com.shellonfire.trackitms.util.JWTTokenHelper;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

@AllArgsConstructor
@Slf4j
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class SecurityConfig {

    private final AdminServerProperties adminServer;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JWTTokenHelper jWTTokenHelper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return auth.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler =
                new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.adminServer.getContextPath() + "/");

        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(this.adminServer.getContextPath() + "/assets/**").permitAll()
                                .requestMatchers(this.adminServer.getContextPath() + "/login").permitAll()
                                .requestMatchers(this.adminServer.getContextPath() + "/instances").permitAll()
                                .requestMatchers(this.adminServer.getContextPath() + "/instances/**").permitAll()
                                .requestMatchers(this.adminServer.getContextPath() + "/actuator/**").permitAll()
                                .requestMatchers("/api/v1/auth/**", "/api/v1/public/**").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .ignoringRequestMatchers(
//                                new AntPathRequestMatcher(this.adminServer.getContextPath() + "/assets/**"),
//                                new AntPathRequestMatcher(this.adminServer.getContextPath() + "/login"),
//                                new AntPathRequestMatcher(this.adminServer.getContextPath() + "/instances"),
//                                new AntPathRequestMatcher(this.adminServer.getContextPath() + "/instances/**"),
//                                new AntPathRequestMatcher(this.adminServer.getContextPath() + "/actuator/**"),
//                                new AntPathRequestMatcher("/**", HttpMethod.OPTIONS.toString())
//                        )
//                        .ignoringRequestMatchers("/api/v1/auth/**", "/api/v1/public/**")
//                )
                .csrf(AbstractHttpConfigurer::disable)
                .rememberMe(rememberMe ->
                        rememberMe
                                .key(UUID.randomUUID().toString())
                                .tokenValiditySeconds(1209600)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(authenticationEntryPoint)
                )
                .addFilterBefore(new JWTAuthenticationFilter(userDetailsService, jWTTokenHelper), UsernamePasswordAuthenticationFilter.class)
                .cors(withDefaults())
                .headers(headers -> headers.frameOptions().disable());

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/api/v1/auth/**", "/api/v1/public/**", "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }
}
