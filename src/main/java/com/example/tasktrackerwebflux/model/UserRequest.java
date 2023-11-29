package com.example.tasktrackerwebflux.model;

import com.example.tasktrackerwebflux.entity.RoleType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

  @NotBlank(message = "Имя пользователя должно быть заполнено!")
  private String username;

  private String email;

  private String password;

  private Set<RoleType> roles;

}
