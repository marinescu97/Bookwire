package com.store.Bookwire.services;

import com.store.Bookwire.models.dtos.AddressDto;
import com.store.Bookwire.models.dtos.UserDto;
import com.store.Bookwire.models.dtos.UserUpdateDto;
import jakarta.validation.Valid;

public interface CustomerService {
    UserDto updateUser(UserUpdateDto dto);
    AddressDto addAddress(@Valid AddressDto dto);
}
