package dev.cascadiatech.trackfi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Web security configuration
 */
@Configuration
@EnableWebSecurity
class SecurityConfig {

  /**
   * Configures spring-security components
   * @return {@link SecurityFilterChain} with spring-security configuration
   */
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
        .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()
        .anyRequest().authenticated()
      ).csrf(AbstractHttpConfigurer::disable)
      .build();
  }

}
