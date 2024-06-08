package com.elearn.eazybank.accounts.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.elearn.eazybank.accounts.entity.Account;

import jakarta.transaction.Transactional;

public interface AccountRepo extends JpaRepository<Account, Long>{

	Optional<Account> findByCustomerId(Long customerId);

	@Transactional
	@Modifying
	void deleteByCustomerId(Long customerId);
}
