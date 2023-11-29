package com.example.tasktrackerwebflux.controller;

import com.example.tasktrackerwebflux.entity.User;
import com.example.tasktrackerwebflux.mapper.UserMapper;
import com.example.tasktrackerwebflux.model.UserRequest;
import com.example.tasktrackerwebflux.model.UserResponse;
import com.example.tasktrackerwebflux.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/public")
@AllArgsConstructor
@Slf4j
public class PublicController {

  private final UserService userService;

  private final UserMapper userMapper;

  @PostMapping
  public Mono<UserResponse> createUser( @RequestBody @Valid UserRequest request ) {
    Mono<User> user = userService.save( userMapper.userRequestToUser( request ) );
    return user.flatMap(  item -> Mono.just( userMapper.userToUserResponse( item ) ) );
  }
}
