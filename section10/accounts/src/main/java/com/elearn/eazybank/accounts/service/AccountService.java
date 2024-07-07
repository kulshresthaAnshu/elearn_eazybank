package com.elearn.eazybank.accounts.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elearn.eazybank.accounts.constants.AccountConstant;
import com.elearn.eazybank.accounts.dto.AccountDTO;
import com.elearn.eazybank.accounts.dto.CustomerDTO;
import com.elearn.eazybank.accounts.entity.Account;
import com.elearn.eazybank.accounts.entity.Customer;
import com.elearn.eazybank.accounts.exception.CustomerAlreadyExistsException;
import com.elearn.eazybank.accounts.exception.ResourceNotFoundException;
import com.elearn.eazybank.accounts.mapper.AccountMapper;
import com.elearn.eazybank.accounts.mapper.CustomerMapper;
import com.elearn.eazybank.accounts.repo.AccountRepo;
import com.elearn.eazybank.accounts.repo.CustomerRepo;

@Service
public class AccountService {

	@Autowired
	AccountRepo accountRepo;
	@Autowired
	CustomerRepo customerRepo;
	@Autowired
	CustomerMapper customerMapper;
	@Autowired
	AccountMapper accountMapper;

	public void createAccount(CustomerDTO customerDTO) {
		Customer customer = customerMapper.convertCustomerDTOtoEntity(customerDTO);
		Optional<Customer> existcustomer = customerRepo.findByMobileNumber(customerDTO.getMobileNumber());
//		customer.setCreatedAt(LocalDateTime.now());
//		customer.setCreatedBy("Anonumous");
		if (existcustomer.isPresent()) {
			throw new CustomerAlreadyExistsException(
					"Customer Exists with given MObile Number" + customerDTO.getMobileNumber());
		}
		Customer saved = customerRepo.save(customer);
		accountRepo.save(this.createNewAccount(saved));
	}

	public Account createNewAccount(Customer customer) {
		Account account = new Account();
		account.setCustomerId(customer.getCustomerId());
		long randomAccountNumber = 1000000000L + new Random().nextInt(90000000);
		account.setAccountNumber(randomAccountNumber);
		account.setAccountType(AccountConstant.SAVINGS);
		account.setBranchAddress(AccountConstant.ADDRESS);
//		account.setCreatedAt(LocalDateTime.now());
//		account.setCreatedBy("Anonumous");
		return account;
	}

	public CustomerDTO fetchAccount(String mobileNumber) {
		Customer customer = customerRepo.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
		Account account = accountRepo.findByCustomerId(customer.getCustomerId()).orElseThrow(
				() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO = customerMapper.convertCustomerEntityToDTO(customer);
		customerDTO.setAccountDto(accountMapper.convertAccountEntityToDTO(account));
		return customerDTO;
	}

	public boolean updateAccount(CustomerDTO customerDto) {
        boolean isUpdated = false;
        AccountDTO accountsDto = customerDto.getAccountDto();
        if(accountsDto !=null ){
            Account account = accountRepo.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            accountMapper.convertAccountEntityToDTO(account);
            account = accountRepo.save(account);

            Long customerId = account.getCustomerId();
            Customer customer = customerRepo.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            customer = customerMapper.convertCustomerDTOtoEntity(customerDto);
            customerRepo.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepo.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountRepo.deleteByCustomerId(customer.getCustomerId());
        customerRepo.deleteById(customer.getCustomerId());
        return true;
    }

}
