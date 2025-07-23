package com.store.Bookwire.services;

import com.store.Bookwire.models.dtos.BookUpdateDto;
import com.store.Bookwire.models.dtos.BookAdminDto;

public interface BookManagementService {
    BookAdminDto save(BookAdminDto dto);
    void deleteById(Long id);
    BookAdminDto updateById(Long id, BookUpdateDto dto);
}
