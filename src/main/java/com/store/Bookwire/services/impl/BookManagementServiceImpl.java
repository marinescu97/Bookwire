package com.store.Bookwire.services.impl;

import com.store.Bookwire.exceptions.ResourceNotFoundException;
import com.store.Bookwire.mappers.BookMapper;
import com.store.Bookwire.models.dtos.BookUpdateDto;
import com.store.Bookwire.models.dtos.BookAdminDto;
import com.store.Bookwire.models.entities.Book;
import com.store.Bookwire.repositories.BookRepository;
import com.store.Bookwire.services.BookManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookManagementServiceImpl implements BookManagementService {
    private final BookRepository repository;
    private final BookMapper mapper;

    @Autowired
    public BookManagementServiceImpl(BookRepository repository, BookMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public BookAdminDto save(BookAdminDto dto) {
        Book book = mapper.toEntity(dto);
        return mapper.toAdminDto(repository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        Book foundBook = findById(id);
        repository.delete(foundBook);
    }

    @Override
    public BookAdminDto updateById(Long id, BookUpdateDto dto) {
        Book foundBook = findById(id);

        mapper.updateFromDto(dto, foundBook);

        return mapper.toAdminDto(repository.save(foundBook));
    }

    private Book findById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found."));
    }
}
