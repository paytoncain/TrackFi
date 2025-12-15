package dev.cascadiatech.trackfi.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Web security configuration
 */
@Configuration
class SecurityConfig {

  /**
   * Configures spring-security components
   * @return {@link SecurityFilterChain} with spring-security configuration
   */
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
        .anyRequest().authenticated()
      ).csrf(AbstractHttpConfigurer::disable)
      .build();
  }

}
