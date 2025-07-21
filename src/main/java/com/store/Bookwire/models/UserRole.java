package com.store.Bookwire.models;

import lombok.Getter;

@Getter
public enum UserRole {
    CUSTOMER("Customer"),
    ADMIN("Admin");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }
}