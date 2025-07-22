package com.store.Bookwire.controllers;

import com.store.Bookwire.security.SecurityUtils;
import com.store.Bookwire.models.dtos.AddressDto;
import com.store.Bookwire.models.dtos.UserDto;
import com.store.Bookwire.models.dtos.UserUpdateDto;
import com.store.Bookwire.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
@Tag(name = "Customer profile")
public class CustomerController {
    private final CustomerService customerService;

    @Operation(summary = "Update user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile was updated successfully"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PatchMapping
    public ResponseEntity<UserDto> updateProfile(@Valid @RequestBody UserUpdateDto dto) {
        return ResponseEntity.ok(customerService.updateUser(dto));
    }

    @Operation(summary = "Add address to user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address was added successfully"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping("/address")
    public ResponseEntity<AddressDto> addAddress(@RequestBody @Valid AddressDto dto) {
        AddressDto addressDto = customerService.addAddress(dto);
        return ResponseEntity.ok(addressDto);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        Object target = binder.getTarget();
        if (target instanceof UserUpdateDto dto) {
            if (dto.getId() == null) {
                dto.setId(SecurityUtils.getCurrentUserId());
            }
        }
    }
}
