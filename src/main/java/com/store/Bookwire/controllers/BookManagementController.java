package com.store.Bookwire.controllers;

import com.store.Bookwire.models.dtos.BookRequestDto;
import com.store.Bookwire.models.dtos.BookUpdateDto;
import com.store.Bookwire.models.dtos.view.BookAdminDto;
import com.store.Bookwire.services.BookManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/books")
@Validated
public class BookManagementController {
    private final BookManagementService service;

    @Autowired
    public BookManagementController(BookManagementService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BookAdminDto> save(@Valid @RequestBody BookRequestDto dto){
        BookAdminDto savedBook = service.save(dto);
        return ResponseEntity.ok(savedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookAdminDto> updateBook(@PathVariable Long id, @Valid @RequestBody BookUpdateDto dto){
        return ResponseEntity.ok(service.updateById(id, dto));
    }

    // Sets dto's id before validation
    @InitBinder
    public void initBinder(WebDataBinder binder, @PathVariable(required = false) Long id) {
        Object target = binder.getTarget();
        if (target instanceof BookUpdateDto dto) {
            if (dto.getId() == null && id != null) {
                dto.setId(id);
            }
        }
    }
}
