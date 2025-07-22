package com.store.Bookwire.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    private String street;
    @EqualsAndHashCode.Include
    @Column(name = "zip_code")
    private String zipCode;
    @EqualsAndHashCode.Include
    private String city;
    @EqualsAndHashCode.Include
    private String state;
}
