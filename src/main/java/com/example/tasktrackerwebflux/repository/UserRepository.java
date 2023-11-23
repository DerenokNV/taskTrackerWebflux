package com.example.tasktrackerwebflux.repository;

import com.example.tasktrackerwebflux.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
