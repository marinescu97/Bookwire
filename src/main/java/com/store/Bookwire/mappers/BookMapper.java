package com.store.Bookwire.mappers;

import com.store.Bookwire.models.dtos.BookRequestDTO;
import com.store.Bookwire.models.dtos.BookUpdateDTO;
import com.store.Bookwire.models.entities.Book;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    Book toEntity(BookRequestDTO dto);

    BookRequestDTO toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(BookUpdateDTO dto, @MappingTarget Book entity);
}
