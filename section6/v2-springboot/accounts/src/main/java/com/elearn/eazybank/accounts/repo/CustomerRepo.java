package com.elearn.eazybank.accounts.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearn.eazybank.accounts.entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
	Optional<Customer> findByMobileNumber(String mobileNumber);

}
