package com.example.tasktrackerwebflux.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

  @NotBlank(message = "Имя пользователя должно быть заполнено!")
  private String username;

  private String email;

}
