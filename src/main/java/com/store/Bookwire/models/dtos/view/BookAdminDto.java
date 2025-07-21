package com.store.Bookwire.models.dtos.view;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BookAdminDto extends BookViewDto {
    private Integer quantity;
}