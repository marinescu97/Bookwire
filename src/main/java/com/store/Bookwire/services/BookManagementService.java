package com.store.Bookwire.services;

import com.store.Bookwire.models.dtos.BookRequestDTO;
import com.store.Bookwire.models.dtos.BookUpdateDTO;
import com.store.Bookwire.models.entities.Book;

public interface BookManagementService {
    Book save(BookRequestDTO dto);
    void deleteById(Long id);
    BookRequestDTO updateById(Long id, BookUpdateDTO dto);
}
