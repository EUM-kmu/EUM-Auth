package com.eum.auth.config;

import com.eum.auth.config.jwt.JwtAuthenticationFilter;
import com.eum.auth.config.jwt.JwtTokenProvider;
import com.eum.auth.handler.CustomAccessDenierHandler;
import com.eum.auth.handler.CustomAuthenticationEntryPoint;
import com.eum.auth.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAccessDenierHandler accessDenierHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    private static final String[] AUTH_WHITELIST ={
            "/**",
            "/","/swagger-ui/index.html","auth-service/api/v2/**","/api/v2/auth/**","**/index.html","/api/docs/v2/**","/v3/**","/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**"
             };
    private final RedisTemplate redisTemplate;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf((csrf) -> csrf.disable());
        http.cors(Customizer.withDefaults());

        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.formLogin((form) -> form.disable());
        http.httpBasic(AbstractHttpConfigurer::disable);

//        http.addFilterBefore(new JwtAuthenticationFilter( redisTemplate,jwtTokenProvider,customUserDetailsService), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling((exceptionHandling )
                -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDenierHandler)
        );

        http.authorizeHttpRequests(authorize -> authorize
//                .requestMatchers(HttpMethod.POST,"/api/v1/profile").hasAuthority(Role.ROLE_UNPROFILE_USER.toString())
//                .requestMatchers(HttpMethod.POST,"/api/v1/bank-account/password").hasAuthority(Role.ROLE_UNPASSWORD_USER.toString())
//                .requestMatchers("/api/v1/**").hasAuthority(Role.ROLE_USER.toString())
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated());
        return http.build();

    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
