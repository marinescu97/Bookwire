package com.store.Bookwire.mappers;

import com.store.Bookwire.models.dtos.auth.RegisterRequest;
import com.store.Bookwire.models.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "role", constant = "CUSTOMER")
    @Mapping(target = "password", source = "password")
    User registerRequestToUser(RegisterRequest request, String password);
}