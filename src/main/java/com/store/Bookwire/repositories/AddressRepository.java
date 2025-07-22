package com.store.Bookwire.repositories;

import com.store.Bookwire.models.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByStreetAndZipCodeAndCityAndState(String street, String zipCode, String city, String state);
}
