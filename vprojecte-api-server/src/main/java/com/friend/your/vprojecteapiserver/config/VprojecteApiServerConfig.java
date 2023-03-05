package com.friend.your.vprojecteapiserver.config;

import com.friend.your.vprojecteapiserver.utils.KCRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
public class VprojecteApiServerConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationConverter jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(new KCRoleConverter());

        http.authorizeHttpRequests()
                .requestMatchers(POST, "/api/register").permitAll()
                .requestMatchers(POST, "/api/login").permitAll()
                .requestMatchers("vasya").hasRole("user")
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthConverter);

        return http.build();

    }

}
