package com.example.tasktrackerwebflux.security;

import com.example.tasktrackerwebflux.entity.User;
import com.example.tasktrackerwebflux.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


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
