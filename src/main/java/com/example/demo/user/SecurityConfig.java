package com.example.demo.user;


import java.util.Collections;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig extends ResourceServerConfigurerAdapter {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable());
    
        http.authorizeHttpRequests(authorize -> 
            authorize
            .requestMatchers("/heartbeat").permitAll()
            .anyRequest().authenticated()
        )
        .httpBasic(withDefaults());
            
        return http.build();
    }
}
