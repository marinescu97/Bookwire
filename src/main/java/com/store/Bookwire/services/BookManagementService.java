package com.store.Bookwire.services;

import com.store.Bookwire.models.dtos.BookRequestDto;
import com.store.Bookwire.models.dtos.BookUpdateDto;
import com.store.Bookwire.models.dtos.view.BookAdminDto;

public interface BookManagementService {
    BookAdminDto save(BookRequestDto dto);
    void deleteById(Long id);
    BookAdminDto updateById(Long id, BookUpdateDto dto);
}
