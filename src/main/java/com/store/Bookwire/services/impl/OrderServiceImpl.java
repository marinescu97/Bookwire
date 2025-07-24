package com.store.Bookwire.services.impl;

import com.store.Bookwire.exceptions.ResourceNotFoundException;
import com.store.Bookwire.models.OrderStatus;
import com.store.Bookwire.models.dtos.OrderDto;
import com.store.Bookwire.models.dtos.OrderItemDto;
import com.store.Bookwire.models.entities.Book;
import com.store.Bookwire.models.entities.Order;
import com.store.Bookwire.models.entities.OrderItem;
import com.store.Bookwire.models.entities.User;
import com.store.Bookwire.repositories.BookRepository;
import com.store.Bookwire.repositories.OrderRepository;
import com.store.Bookwire.repositories.UserRepository;
import com.store.Bookwire.security.SecurityUtils;
import com.store.Bookwire.services.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public Order placeOrder(OrderDto orderDto) {
        User user = getCurrentUser();
        Order order = createOrderForUser(user);

        List<OrderItem> items = buildOrderItems(order, orderDto.getItems());
        order.setItems(items);

        return orderRepository.save(order);
    }

    private User getCurrentUser() {
        return userRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Order createOrderForUser(User user) {
        Order order = new Order();
        order.setCustomer(user);
        order.setStatus(OrderStatus.PLACED);
        return order;
    }

    private List<OrderItem> buildOrderItems(Order order, List<OrderItemDto> itemDtoList) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDto dto : itemDtoList) {
            Book book = validateAndUpdateBookStock(dto.getBookId(), dto.getQuantity());

            OrderItem item = new OrderItem();
            item.setBook(book);
            item.setOrder(order);
            item.setQuantity(dto.getQuantity());

            orderItems.add(item);
        }

        return orderItems;
    }

    private Book validateAndUpdateBookStock(Long bookId, int requestedQty) {
        Book book = findBook(bookId);

        if (book.getQuantity() < requestedQty) {
            throw new RuntimeException("Not enough stock for book: " + book.getTitle());
        }

        book.setQuantity(book.getQuantity() - requestedQty);
        bookRepository.save(book);

        return book;
    }

    private Book findBook(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }
}
