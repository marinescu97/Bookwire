package com.store.Bookwire.controllers;

import com.store.Bookwire.models.dtos.BookRequestDto;
import com.store.Bookwire.models.dtos.BookUpdateDto;
import com.store.Bookwire.models.dtos.view.BookAdminDto;
import com.store.Bookwire.services.BookManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/books")
@Validated
@Tag(name = "Book management")
@SecurityRequirement(name = "bearerAuth")
public class BookManagementController {
    private final BookManagementService service;

    @Autowired
    public BookManagementController(BookManagementService service) {
        this.service = service;
    }

    @Operation(summary = "Create a new book", operationId = "1_save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    public ResponseEntity<BookAdminDto> save(@Valid @RequestBody BookRequestDto dto){
        BookAdminDto savedBook = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @Operation(summary = "Delete a book", operationId = "3_delete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a book", operationId = "2_update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
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
