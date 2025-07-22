package com.store.Bookwire.services.impl;

import com.store.Bookwire.security.SecurityUtils;
import com.store.Bookwire.exceptions.ResourceNotFoundException;
import com.store.Bookwire.mappers.AddressMapper;
import com.store.Bookwire.mappers.UserMapper;
import com.store.Bookwire.models.dtos.AddressDto;
import com.store.Bookwire.models.dtos.UserDto;
import com.store.Bookwire.models.dtos.UserUpdateDto;
import com.store.Bookwire.models.entities.Address;
import com.store.Bookwire.models.entities.User;
import com.store.Bookwire.repositories.AddressRepository;
import com.store.Bookwire.repositories.UserRepository;
import com.store.Bookwire.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerServiceImpl implements CustomerService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final PasswordEncoder encoder;

    @Override
    public UserDto updateUser(UserUpdateDto dto){
        User user = findUser();
        userMapper.updateUserFromDto(dto, user);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(encoder.encode(dto.getPassword()));
        }

        return  userMapper.toDto(userRepository.save(user));
    }

    @Override
    public AddressDto addAddress(AddressDto dto) {
        User user = findUser();
        Address address = findOrCreateAddress(dto);

        user.setAddress(address);
        userRepository.save(user);

        return addressMapper.toDto(address);
    }

    private Address findOrCreateAddress(AddressDto dto) {
        Optional<Address> existingAddress = addressRepository.findByStreetAndZipCodeAndCityAndState(
                dto.getStreet(), dto.getZipCode(), dto.getCity(), dto.getState()
        );

        Address address;
        if (existingAddress.isPresent()) {
            address = existingAddress.get();
        } else {
            address = addressMapper.toEntity(dto);
            address = addressRepository.save(address);
        }
        return address;
    }

    private User findUser(){
        return userRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
