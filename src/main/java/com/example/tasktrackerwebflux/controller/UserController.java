package com.example.tasktrackerwebflux.controller;

import com.example.tasktrackerwebflux.entity.User;
import com.example.tasktrackerwebflux.mapper.UserMapper;
import com.example.tasktrackerwebflux.model.UserRequest;
import com.example.tasktrackerwebflux.model.UserResponse;
import com.example.tasktrackerwebflux.publisher.UserPublisher;
import com.example.tasktrackerwebflux.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Slf4j
public class UserController {

  private final UserService userService;

  private final UserMapper userMapper;

  private final UserPublisher publisher;

  @GetMapping
  public Flux<UserResponse> getAllUsers() {
    Flux<User> users = userService.findAll();
    return users.flatMap( item -> Flux.just( userMapper.userToUserResponse( item ) ) )
                .doOnNext( publisher::publisher );
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<UserResponse>> getById( @PathVariable String id ) {
    Mono<User> user = userService.findById( id );
    return user.flatMap( item -> Mono.just( userMapper.userToUserResponse( item ) ) )
               .map(  ResponseEntity::ok )
               .defaultIfEmpty( ResponseEntity.notFound().build() );
  }

  @PutMapping("/{id}")
  public Mono<UserResponse> updateUser( @PathVariable String id,
                                        @RequestBody UserRequest request ) {
    Mono<User> user = userService.update( id, userMapper.userRequestToUser( request ) );
    return user.flatMap( item -> Mono.just( userMapper.userToUserResponse( item ) ) );
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> deleteUser( @PathVariable String id ) {
    return userService.deleteById( id ).then( Mono.just( ResponseEntity.noContent().build() ) );
  }

  @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent<UserResponse>> getUserStream() {
    return publisher.getUpdateSink()
            .asFlux()
            .map( item -> ServerSentEvent.builder( item ).build() );
  }

}
