package com.example.tasktrackerwebflux.model;

import com.example.tasktrackerwebflux.entity.TaskStatus;
import com.example.tasktrackerwebflux.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

  private String id;

  private String name;

  private String description;

  private Instant createdAt;

  private Instant updateAt;

  private TaskStatus status;

  private User author;

  private User assignee;

  private Set<User> observers = new HashSet<>();
}
