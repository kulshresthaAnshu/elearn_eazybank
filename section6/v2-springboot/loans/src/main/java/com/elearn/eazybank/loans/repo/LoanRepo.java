package com.elearn.eazybank.loans.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elearn.eazybank.loans.entity.Loan;

@Repository
public interface LoanRepo extends JpaRepository<Loan, Long> {

	Optional<Loan> findByMobileNumber(String mobileNumber);

	Optional<Loan> findByLoanNumber(String cardNumber);

}