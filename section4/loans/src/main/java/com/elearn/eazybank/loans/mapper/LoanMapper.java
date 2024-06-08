package com.elearn.eazybank.loans.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elearn.eazybank.loans.dto.LoanDTO;
import com.elearn.eazybank.loans.entity.Loan;

@Component
public class LoanMapper extends ModelMapper {
	@Autowired
	ModelMapper modelMapper;

	public LoanDTO convertLoanEntityToDTO(Loan entity) {
		LoanDTO LoanDTO = new LoanDTO();
		modelMapper.map(entity, LoanDTO);
		return LoanDTO;
	}

	public Loan convertLoanDTOtoEntity(LoanDTO LoanDTO) {
		Loan entity = new Loan();
		modelMapper.map(LoanDTO, entity);
		return entity;
	}

	public Loan convertLoanDTOtoEntity(LoanDTO LoanDTO, Loan entity) {
		modelMapper.map(LoanDTO, entity);
		return entity;
	}
}
