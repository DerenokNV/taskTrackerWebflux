package com.example.tasktrackerwebflux.service;

import com.example.tasktrackerwebflux.entity.User;
import com.example.tasktrackerwebflux.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  public Flux<User> findAll() {
    return userRepository.findAll();
  }

  public Mono<User> findById( String id ) {
    return userRepository.findById( id );
  }

  public Mono<User> save( User user ) {
    user.setId( UUID.randomUUID().toString() );
    user.setPassword( passwordEncoder.encode( user.getPassword() ) );
    return userRepository.save( user );
  }

  public Mono<User> update( String id, User user ) {
    return findById( id ).flatMap(
             userForUpdate -> {
               if ( StringUtils.hasText( user.getUsername() ) ) {
                 userForUpdate.setUsername( user.getUsername() );
               }
               if ( StringUtils.hasText( user.getEmail() ) ) {
                 userForUpdate.setEmail( user.getEmail() );
               }

               return userRepository.save( userForUpdate );
             });
  }

  public Mono<Void> deleteById( String id ) {
    return userRepository.deleteById( id );
  }

}
