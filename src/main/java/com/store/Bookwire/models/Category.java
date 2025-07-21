package com.store.Bookwire.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Category of the book")
public enum Category {
    FICTION("Fiction"),
    NON_FICTION("Non-Fiction"),
    SCIENCE("Science"),
    HISTORY("History"),
    BIOGRAPHY("Biography"),
    CHILDREN("Children"),
    FANTASY("Fantasy"),
    ROMANCE("Romance"),
    MYSTERY("Mystery"),
    HORROR("Horror"),
    BUSINESS("Business"),
    TECHNOLOGY("Technology"),
    EDUCATION("Education"),
    SOFTWARE_DEVELOPMENT("Software Development"),
    PROGRAMMING("Programming"),
    ENGINEERING("Engineering"),
    COMICS("Comics"),
    POETRY("Poetry"),
    TRAVEL("Travel"),
    COOKING("Cooking"),
    HEALTH("Health & Fitness");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }
}
