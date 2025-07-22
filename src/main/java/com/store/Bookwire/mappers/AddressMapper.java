package com.store.Bookwire.mappers;

import com.store.Bookwire.models.dtos.AddressDto;
import com.store.Bookwire.models.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "id", ignore = true)
    Address toEntity(AddressDto dto);

    AddressDto toDto(Address address);
}
