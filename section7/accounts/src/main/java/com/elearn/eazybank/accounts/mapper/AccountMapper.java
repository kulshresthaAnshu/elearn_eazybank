package com.elearn.eazybank.accounts.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elearn.eazybank.accounts.dto.AccountDTO;
import com.elearn.eazybank.accounts.entity.Account;
@Component
public class AccountMapper extends ModelMapper {
	@Autowired
	ModelMapper modelMapper;

	public AccountDTO convertAccountEntityToDTO(Account entity) {
		AccountDTO accountDTO = new AccountDTO();
		modelMapper.map(entity, accountDTO);
		return accountDTO;
	}

	public Account convertAccountDTOtoEntity(AccountDTO accountDTO) {
		Account entity = new Account();
		modelMapper.map(accountDTO, entity);
		return entity;
	}
}
