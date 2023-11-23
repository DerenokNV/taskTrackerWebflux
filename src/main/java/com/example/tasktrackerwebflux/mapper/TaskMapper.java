package com.example.tasktrackerwebflux.mapper;

import com.example.tasktrackerwebflux.entity.Task;
import com.example.tasktrackerwebflux.entity.TaskStatus;
import com.example.tasktrackerwebflux.model.TaskRequest;
import com.example.tasktrackerwebflux.model.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

  Task taskRequestToTask( TaskRequest taskRequest );

  TaskResponse taskToTaskResponse( Task task );

  default Task taskRequestToTaskMapper( TaskRequest taskRequest ) {
    Task task = taskRequestToTask( taskRequest );
    task.setStatus( TaskStatus.valueOf( taskRequest.getStatus() ) );

    return task;
  }

}
