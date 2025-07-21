package com.store.Bookwire.models.dtos;

import com.store.Bookwire.models.entities.Book;
import com.store.Bookwire.validators.Unique;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Unique.List({
        @Unique(entity = Book.class, fieldName = "title", message = "Title already exists."),
        @Unique(entity = Book.class, fieldName = "isbn", message = "ISBN already exists.")
})
public class BookUpdateDto extends BookRequestDto {
    @NotNull(message = "Id must not be null.")
    private Long id;
}