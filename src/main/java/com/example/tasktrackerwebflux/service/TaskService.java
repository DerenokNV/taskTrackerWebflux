package com.example.tasktrackerwebflux.service;

import com.example.tasktrackerwebflux.entity.Task;
import com.example.tasktrackerwebflux.entity.User;
import com.example.tasktrackerwebflux.repository.TaskRepository;
import com.example.tasktrackerwebflux.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
public class TaskService {

  private final TaskRepository taskRepository;

  private final UserRepository userRepository;

  public Flux<Task> findAll() {
    Flux<Task> tasks = taskRepository.findAll();

    Flux<User> userAuthor = tasks.flatMap( x -> { return userRepository.findById( x.getAuthorId() ); });

    Flux<User> userAssignee = tasks.flatMap( x -> { return userRepository.findById( x.getAssigneeId() ); });

    Flux<List<User>> userObObserver = tasks.flatMap( user -> {
                       Set<Mono<User>> monoset = new HashSet<>();
                       for ( String observId : user.getObserverIds() ) {
                         monoset.add( userRepository.findById( observId ) );
                       }

                       Flux merged = Flux.empty();
                       for ( Mono out : monoset ) {
                         merged = merged.mergeWith(out);
                       }
                       return merged.collectList();
                     });

    Flux<Task> resultFlux = Flux.zip( tasks, userAuthor, userObObserver, userAssignee )
            .flatMap( dFlux -> Flux.just( new Task( dFlux.getT1().getId(),
                                                    dFlux.getT1().getDescription(),
                                                    dFlux.getT1().getName(),
                                                    dFlux.getT1().getCreatedAt(),
                                                    dFlux.getT1().getUpdateAt(),
                                                    dFlux.getT1().getStatus(),
                                                    dFlux.getT1().getAuthorId(),
                                                    dFlux.getT1().getAssigneeId(),
                                                    dFlux.getT1().getObserverIds(),
                                                    dFlux.getT2(),
                                                    dFlux.getT4(),
                                                    new HashSet<>( dFlux.getT3() )
                                                  )
                                        ) );


    return resultFlux;
  }

  private Mono<User> getUserAuthorOrAssignee( Mono<Task> findTask, boolean itAuthor ) {
    return findTask.flatMap( user -> {
             return userRepository.findById( itAuthor ? user.getAuthorId() : user.getAssigneeId() );
           });
  }

  private Mono<List<User>> getObserverUsers( Mono<Task> findTask ) {
    return findTask.flatMap( user -> {
                              Set<Mono<User>> monoset = new HashSet<>();
                              for ( String observId : user.getObserverIds() ) {
                                monoset.add( userRepository.findById( observId ) );
                              }

                              Flux merged = Flux.empty();
                              for ( Mono out : monoset ) {
                                merged = merged.mergeWith(out);
                              }
                              return merged.collectList();
                           });
  }

  public Mono<Task> findById( String id ) {
    Mono<Task> findTask = taskRepository.findById( id );

    return findTask.zipWhen( task -> getUserAuthorOrAssignee( findTask, true ), ( t1, t2 ) -> {
                                 t1.setAuthor( t2 );
                                 return t1;
                           })
                   .zipWhen( task -> getUserAuthorOrAssignee( findTask, false ), ( t1, t2 ) -> {
                                 t1.setAssignee( t2 );
                                 return t1;
                           })
                   .zipWhen( task -> getObserverUsers( findTask ), ( t1, t2 ) -> {
                               t1.setObservers( new HashSet<>( t2 ) );
                               return t1;
                           });

  }

  public Mono<Task> save( Task task ) {
    task.setId( UUID.randomUUID().toString() );
    task.setCreatedAt( Instant.now() );
    return taskRepository.save( task );
  }

  public Mono<Task> update( String id, Task task ) {
    Mono<Task> result = findById( id ).flatMap(
            taskForUpdate -> {
              if ( StringUtils.hasText( task.getName() ) ) {
                taskForUpdate.setName( task.getName() );
              }
              if ( StringUtils.hasText( task.getDescription() ) ) {
                taskForUpdate.setDescription( task.getDescription() );
              }
              if ( task.getStatus() != null ) {
                taskForUpdate.setStatus( task.getStatus() );
              }
              if ( StringUtils.hasText( task.getAuthorId() ) ) {
                taskForUpdate.setAuthorId( task.getAuthorId() );
              }
              if ( StringUtils.hasText( task.getAssigneeId() ) ) {
                taskForUpdate.setAssigneeId( task.getAssigneeId() );
              }
              if ( task.getObserverIds() != null ) {
                taskForUpdate.setObserverIds( task.getObserverIds() );
              }
              taskForUpdate.setUpdateAt( Instant.now() );

              return taskRepository.save( taskForUpdate );
            });

    return  result.zipWhen( tt -> getUserAuthorOrAssignee( result, true ), ( t1, t2 ) -> {
                             t1.setAuthor( t2 );
                             return t1;
                          })
                  .zipWhen( tt -> getUserAuthorOrAssignee( result, false ), ( t1, t2 ) -> {
                             t1.setAssignee( t2 );
                             return t1;
                          })
                  .zipWhen( tt -> getObserverUsers( result ), ( t1, t2 ) -> {
                            t1.setObservers( new HashSet<>( t2 ) );
                            return t1;
                          });
  }

  public Mono<Task> taskAddObserver( String idTask, String idObserver ) {
    Mono<Task> result = findById( idTask ).flatMap(
                          taskForUpdate -> {
                            if ( idObserver != null ) {
                              taskForUpdate.getObserverIds().add( idObserver );
                            }
                            return taskRepository.save( taskForUpdate );
                          });
    return  result.zipWhen( tt -> getObserverUsers( result ), ( t1, t2 ) -> {
                            t1.setObservers( new HashSet<>( t2 ) );
                            return t1;
                          });
  }

  public Mono<Void> deleteById( String id ) {
    return taskRepository.deleteById( id );
  }

}
