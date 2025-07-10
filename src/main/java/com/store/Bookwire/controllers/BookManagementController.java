package com.store.Bookwire.controllers;

import com.store.Bookwire.models.dtos.BookRequestDTO;
import com.store.Bookwire.models.entities.Book;
import com.store.Bookwire.services.BookManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookManagementController {
    private final BookManagementService service;

    @Autowired
    public BookManagementController(BookManagementService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Book> save(@Valid @RequestBody BookRequestDTO dto){
        Book savedBook = service.save(dto);
        return ResponseEntity.ok(savedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookRequestDTO> updateBook(@PathVariable Long id,@Valid @RequestBody BookRequestDTO dto){
        return ResponseEntity.ok(service.updateById(id, dto));
    }
}
