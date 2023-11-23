package com.example.tasktrackerwebflux.mapper;

import com.example.tasktrackerwebflux.entity.User;
import com.example.tasktrackerwebflux.model.UserRequest;
import com.example.tasktrackerwebflux.model.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  UserResponse userToUserResponse( User user );

  User userRequestToUser( UserRequest userRequest );

  User userMapperIdToUser( String Id, UserMapper userMapper );
}
