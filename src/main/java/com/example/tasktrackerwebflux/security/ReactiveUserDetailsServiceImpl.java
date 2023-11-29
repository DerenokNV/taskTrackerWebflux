package com.example.tasktrackerwebflux.security;

import com.example.tasktrackerwebflux.entity.RoleType;
import com.example.tasktrackerwebflux.entity.User;
import com.example.tasktrackerwebflux.repository.UserRepository;
import com.example.tasktrackerwebflux.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

  private final UserRepository userRepository;

  @Override
  public Mono<UserDetails> findByUsername( String username ) {
    return userRepository.findByUsername( username )
            .flatMap( Mono::just )
            .cast( User.class )
            .map( AppUserPrincipal::new );
  }
}
