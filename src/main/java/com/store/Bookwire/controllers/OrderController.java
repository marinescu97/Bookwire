package com.store.Bookwire.controllers;

import com.store.Bookwire.models.dtos.OrderDto;
import com.store.Bookwire.models.entities.Order;
import com.store.Bookwire.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/orders")
@Validated
public class OrderController {
    private final OrderService service;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> placeOrder(
            @RequestBody OrderDto request) {

        Order order = service.placeOrder(request);
        return ResponseEntity.ok(order);
    }
}
