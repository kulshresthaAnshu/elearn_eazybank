package com.elearn.eazybank.loans.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elearn.eazybank.loans.constants.LoanConstants;
import com.elearn.eazybank.loans.dto.LoanDTO;
import com.elearn.eazybank.loans.entity.Loan;
import com.elearn.eazybank.loans.exception.LoanAlreadyExistsException;
import com.elearn.eazybank.loans.exception.ResourceNotFoundException;
import com.elearn.eazybank.loans.mapper.LoanMapper;
import com.elearn.eazybank.loans.repo.LoanRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoanService {

	@Autowired
	LoanRepo loanRepo;

	@Autowired
	LoanMapper loanMapper;

	/**
	 * @param mobileNumber - Mobile Number of the Customer
	 */
	public void createLoan(String mobileNumber) {
		Optional<Loan> optionalLoan = loanRepo.findByMobileNumber(mobileNumber);
		if (optionalLoan.isPresent()) {
			throw new LoanAlreadyExistsException("Loan already registered with given mobileNumber " + mobileNumber);
		}
		loanRepo.save(createNewLoan(mobileNumber));
	}

	/**
	 * @param mobileNumber - Mobile Number of the Customer
	 * @return the new loan details
	 */
	private Loan createNewLoan(String mobileNumber) {
		Loan newLoan = new Loan();
		long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
		newLoan.setLoanNumber(Long.toString(randomLoanNumber));
		newLoan.setMobileNumber(mobileNumber);
		newLoan.setLoanType(LoanConstants.HOME_LOAN);
		newLoan.setTotalLoan(LoanConstants.NEW_LOAN_LIMIT);
		newLoan.setAmountPaid(0);
		newLoan.setOutstandingAmount(LoanConstants.NEW_LOAN_LIMIT);
		return newLoan;
	}

	/**
	 *
	 * @param mobileNumber - Input mobile Number
	 * @return Loan Details based on a given mobileNumber
	 */
	public LoanDTO fetchLoan(String mobileNumber) {
		Loan loans = loanRepo.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber));
		return loanMapper.convertLoanEntityToDTO(loans);
	}

	/**
	 *
	 * @param loansDto - LoanDTO Object
	 * @return boolean indicating if the update of loan details is successful or not
	 */
	public boolean updateLoan(LoanDTO loansDto) {
		Loan loans = loanRepo.findByLoanNumber(loansDto.getLoanNumber())
				.orElseThrow(() -> new ResourceNotFoundException("Loan", "LoanNumber", loansDto.getLoanNumber()));
		loanMapper.convertLoanDTOtoEntity(loansDto, loans);
		loanRepo.save(loans);
		return true;
	}

	/**
	 * @param mobileNumber - Input MobileNumber
	 * @return boolean indicating if the delete of loan details is successful or not
	 */
	public boolean deleteLoan(String mobileNumber) {
		Loan loans = loanRepo.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber));
		loanRepo.deleteById(loans.getLoanId());
		return true;
	}

}