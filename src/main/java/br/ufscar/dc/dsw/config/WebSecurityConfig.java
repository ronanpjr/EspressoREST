package br.ufscar.dc.dsw.config;

import br.ufscar.dc.dsw.security.JwtAuthenticationFilter;
import br.ufscar.dc.dsw.security.JwtAuthorizationFilter;
import br.ufscar.dc.dsw.security.TokenProvider;
import br.ufscar.dc.dsw.services.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private static final String[] SWAGGER_ENDPOINTS = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**"
    };


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager, TokenProvider tokenProvider, UsuarioService usuarioService) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless API
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Set session management to stateless
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints
                        .requestMatchers("/api/login", "/error").permitAll()
                        .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/estrategias", "/api/estrategias/**").permitAll()

                        // Protected endpoints
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/estrategias").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/estrategias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/estrategias/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                // Add our custom JWT filters
                .addFilter(new JwtAuthenticationFilter(authenticationManager, tokenProvider))
                .addFilterBefore(new JwtAuthorizationFilter(tokenProvider, usuarioService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}