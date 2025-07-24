package com.store.Bookwire.services;

import com.store.Bookwire.models.dtos.OrderDto;
import com.store.Bookwire.models.entities.Order;

public interface OrderService {
    Order placeOrder(OrderDto dto);
}
