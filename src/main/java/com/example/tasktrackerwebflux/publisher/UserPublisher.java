package com.example.tasktrackerwebflux.publisher;

import com.example.tasktrackerwebflux.model.UserResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
public class UserPublisher {
  private final Sinks.Many<UserResponse> userModelSink;

  public UserPublisher() {
    this.userModelSink = Sinks.many().multicast().onBackpressureBuffer();
  }

  public void publisher( UserResponse model ) {
    userModelSink.tryEmitNext( model );
  }

  public Sinks.Many<UserResponse> getUpdateSink() {
    return userModelSink;
  }

}
