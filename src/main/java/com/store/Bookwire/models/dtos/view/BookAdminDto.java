package com.store.Bookwire.models.dtos.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO used for displaying a book for an admin")
public class BookAdminDto extends BookViewDto {
    @Schema(description = "Number of copies available in stock")
    private Integer quantity;
}