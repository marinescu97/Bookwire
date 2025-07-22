package com.store.Bookwire.mappers;

import com.store.Bookwire.models.dtos.UserDto;
import com.store.Bookwire.models.dtos.UserUpdateDto;
import com.store.Bookwire.models.dtos.auth.RegisterRequest;
import com.store.Bookwire.models.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "role", constant = "CUSTOMER")
    @Mapping(target = "password", source = "password")
    User registerRequestToUser(RegisterRequest request, String password);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "role", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserUpdateDto dto, @MappingTarget User user);

    UserDto toDto(User user);
}