package com.example.tasktrackerwebflux.configuration;

import com.example.tasktrackerwebflux.security.ReactiveUserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public ReactiveAuthenticationManager authenticationManager( ReactiveUserDetailsServiceImpl userDetailsService,
                                                              PasswordEncoder passwordEncoder ) {
    var reactiveAuthenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager( userDetailsService );

    reactiveAuthenticationManager.setPasswordEncoder( passwordEncoder );

    return reactiveAuthenticationManager;
  }

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain( ServerHttpSecurity http, ReactiveAuthenticationManager authenticationManager ) {
    return http.csrf( ServerHttpSecurity.CsrfSpec::disable )
               .authorizeExchange( auth -> auth
                       .pathMatchers( "/api/v1/users/**" ).hasAnyRole( "USER", "MANAGER" )
                       .pathMatchers( "/api/v1/tasks/**" ).hasAnyRole( "USER", "MANAGER" )
                       .pathMatchers( "/api/v1/public/**" ).permitAll()
                       .anyExchange().authenticated() )
               .httpBasic( Customizer.withDefaults())
               .authenticationManager( authenticationManager )
               .build();
  }

}
