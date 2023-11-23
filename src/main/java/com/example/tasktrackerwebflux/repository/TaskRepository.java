package com.example.tasktrackerwebflux.repository;

import com.example.tasktrackerwebflux.entity.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TaskRepository extends ReactiveMongoRepository<Task, String> {
}
