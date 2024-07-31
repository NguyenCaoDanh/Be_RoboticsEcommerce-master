package com.wisdom.roboticsecommerce.Config;


import com.google.common.collect.ImmutableList;
import com.wisdom.roboticsecommerce.Security.UserPrincical.Userdetail;
import com.wisdom.roboticsecommerce.Security.jwt.JwtEntryPoint;
import com.wisdom.roboticsecommerce.Security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static com.wisdom.roboticsecommerce.Config.ConfigApi.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtEntryPoint jwtEntryPoint;
    @Autowired
    private Userdetail userdetail;
    @Bean
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter();
    }
        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userdetail);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("Access-Control-Allow-Origin"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(false);
        configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http
//                .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PUBLIC_API).permitAll()
                        .requestMatchers(ADMIN_API).hasAnyAuthority("ADMIN")
                        .requestMatchers(USER_API).hasAnyAuthority("USER")
                        .requestMatchers(ADMIN_USER_API).hasAnyAuthority("ADMIN","USER")
                        .anyRequest().authenticated())
                .logout(logout -> logout
                        .logoutUrl("*/logout/**")
                        .invalidateHttpSession(true));
        http
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
