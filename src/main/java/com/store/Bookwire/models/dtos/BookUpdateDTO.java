package com.store.Bookwire.models.dtos;

import com.store.Bookwire.models.entities.Book;
import com.store.Bookwire.validators.Unique;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Unique.List({
        @Unique(entity = Book.class, fieldName = "title", message = "Title already exists."),
        @Unique(entity = Book.class, fieldName = "isbn", message = "ISBN already exists.")
})
public class BookUpdateDTO extends BookRequestDTO implements Serializable {
    @NotNull(message = "Id must not be null.")
    private Long id;

}
