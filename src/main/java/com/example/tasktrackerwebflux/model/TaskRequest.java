package com.example.tasktrackerwebflux.model;

import com.example.tasktrackerwebflux.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

  @NotBlank(message = "Имя задачи должно быть заполнено!")
  private String name;

  private String description;

  private String status;

  private String authorId;

  private String assigneeId;

  private Set<String> observerIds;
}
