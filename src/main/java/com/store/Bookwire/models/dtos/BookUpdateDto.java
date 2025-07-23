package com.store.Bookwire.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Schema(description = "Data transfer object used for updating a book")
public class BookUpdateDto extends BookAdminDto {
    @NotNull(message = "Id must not be null.")
    @Schema(description = "The ID of the book to be updated", example = "5")
    private Long id;
}