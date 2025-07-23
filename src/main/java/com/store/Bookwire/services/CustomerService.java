package com.store.Bookwire.services;

import com.store.Bookwire.models.dtos.AddressDto;
import com.store.Bookwire.models.dtos.UserDto;
import com.store.Bookwire.models.dtos.UserUpdateDto;

public interface CustomerService {
    UserDto updateUser(UserUpdateDto dto);
    AddressDto addAddress(AddressDto dto);
}
