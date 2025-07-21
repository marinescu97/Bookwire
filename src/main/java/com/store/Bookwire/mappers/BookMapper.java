package com.store.Bookwire.mappers;

import com.store.Bookwire.models.dtos.BookRequestDto;
import com.store.Bookwire.models.dtos.BookUpdateDto;
import com.store.Bookwire.models.dtos.view.BookAdminDto;
import com.store.Bookwire.models.dtos.view.BookCustomerDto;
import com.store.Bookwire.models.entities.Book;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    Book toEntity(BookRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(BookUpdateDto dto, @MappingTarget Book entity);

    BookAdminDto toAdminDto(Book book);

    @Mapping(target = "stock", expression = "java(mapStock(book.getQuantity()))")
    BookCustomerDto toCustomerDto(Book book);

    default String mapStock(int quantity) {
        return quantity > 0 ? "In stock" : "Out of stock";
    }
}
