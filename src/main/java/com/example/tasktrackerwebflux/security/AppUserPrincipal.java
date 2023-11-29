package com.example.tasktrackerwebflux.security;

import com.example.tasktrackerwebflux.entity.RoleType;
import com.example.tasktrackerwebflux.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AppUserPrincipal implements UserDetails {

  private final User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getRoles().stream().map( x -> new SimpleGrantedAuthority( x.name() ) ).collect( Collectors.toSet() );
  }

  public String getUserId() {
    return user.getId();
  }

  @Override
  public String getPassword() {

    return user.getPassword();
  }

  @Override
  public String getUsername() {

    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
