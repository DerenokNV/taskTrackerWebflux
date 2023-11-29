package com.example.tasktrackerwebflux.repository;

import com.example.tasktrackerwebflux.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

  Mono<User> findByUsername( String username);
}
