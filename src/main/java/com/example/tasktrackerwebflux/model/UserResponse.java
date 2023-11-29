package com.example.tasktrackerwebflux.model;

import com.example.tasktrackerwebflux.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

  private String id;

  private String username;

  private String email;

  private String password;

  private Set<RoleType> roles;
}
