package com.example.tasktrackerwebflux.controller;

import com.example.tasktrackerwebflux.entity.Task;
import com.example.tasktrackerwebflux.mapper.TaskMapper;
import com.example.tasktrackerwebflux.model.TaskAddObserverRequest;
import com.example.tasktrackerwebflux.model.TaskRequest;
import com.example.tasktrackerwebflux.model.TaskResponse;
import com.example.tasktrackerwebflux.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/tasks")
@AllArgsConstructor
@Slf4j
public class TaskController {

  private final TaskService taskService;

  private final TaskMapper taskMapper;


  @GetMapping
  public Flux<TaskResponse> getAllTasks() {
    Flux<Task> tasks = taskService.findAll();
    return tasks.flatMap( item -> {
              return Flux.just( taskMapper.taskToTaskResponse( item ) );
            } );
  }

  @PostMapping
  public Mono<TaskResponse> createTask( @RequestBody @Valid TaskRequest request ) {
    Mono<Task> task = taskService.save( taskMapper.taskRequestToTaskMapper( request ) );
    return task.flatMap(  item -> {
             return Mono.just( taskMapper.taskToTaskResponse( item ) );
           });
  }

  @GetMapping("/{id}")
  public Mono<TaskResponse> getTaskById( @PathVariable String id ) {
    Mono<Task> task = taskService.findById( id );
    return task.flatMap(  item -> {
                            return Mono.just( taskMapper.taskToTaskResponse( item ) );
                       });
  }

  @PutMapping("/{id}")
  public Mono<TaskResponse> updateTask( @PathVariable String id,
                                        @RequestBody TaskRequest request ) {
    Mono<Task> task = taskService.update( id, taskMapper.taskRequestToTaskMapper( request ) );
    return task.flatMap(  item -> {
             return Mono.just( taskMapper.taskToTaskResponse( item ) );
           });
  }

  @PutMapping("/task-add-observer/{id}")
  public Mono<TaskResponse> taskAddObserver( @PathVariable String id,
                                             @RequestBody TaskAddObserverRequest request ) {
    Mono<Task> task = taskService.taskAddObserver( id, request.getObserverId() );
    return task.flatMap(  item -> {
             return Mono.just( taskMapper.taskToTaskResponse( item ) );
           });
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> deleteTask( @PathVariable String id ) {
    return taskService.deleteById( id ).then( Mono.just( ResponseEntity.noContent().build() ) );
  }

}
