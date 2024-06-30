package com.elearn.eazybank.accounts.service;

import com.elearn.eazybank.accounts.dto.CardDTO;
import com.elearn.eazybank.accounts.dto.CustomerDetailDTO;
import com.elearn.eazybank.accounts.dto.LoanDTO;
import com.elearn.eazybank.accounts.entity.Account;
import com.elearn.eazybank.accounts.entity.Customer;
import com.elearn.eazybank.accounts.exception.ResourceNotFoundException;
import com.elearn.eazybank.accounts.mapper.AccountMapper;
import com.elearn.eazybank.accounts.mapper.CustomerMapper;
import com.elearn.eazybank.accounts.repo.AccountRepo;
import com.elearn.eazybank.accounts.repo.CustomerRepo;
import com.elearn.eazybank.accounts.service.client.CardsFeignClient;
import com.elearn.eazybank.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {


    private AccountRepo accountRepo;
    private CustomerRepo customerRepo;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;
    private AccountMapper accountMapper;
    public CustomerDetailDTO fetchCustomerDeatils(String mobileNumber){
        Customer customer = customerRepo.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Account account = accountRepo.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

        CustomerDetailDTO customerDetailDTO = CustomerMapper.mapToCustomerDetailsDTO(customer, new CustomerDetailDTO());
        customerDetailDTO.setAccountDto(accountMapper.convertAccountEntityToDTO(account));

        ResponseEntity<LoanDTO> loanDTOResponseEntity = loansFeignClient.fetchLoanDetails(customerDetailDTO.getMobileNumber());
        customerDetailDTO.setLoansDto(loanDTOResponseEntity.getBody());
        ResponseEntity<CardDTO> cardDTOResponseEntity = cardsFeignClient.fetchCardDetails(customerDetailDTO.getMobileNumber());
        customerDetailDTO.setCardsDto(cardDTOResponseEntity.getBody());
        return customerDetailDTO;
    }
}
