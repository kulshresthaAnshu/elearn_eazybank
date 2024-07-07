package com.elearn.eazybank.accounts.mapper;

import com.elearn.eazybank.accounts.dto.CustomerDetailDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elearn.eazybank.accounts.dto.CustomerDTO;
import com.elearn.eazybank.accounts.entity.Customer;

@Component
public class CustomerMapper extends ModelMapper {

	@Autowired
	ModelMapper modelMapper;

	public CustomerDTO convertCustomerEntityToDTO(Customer entity) {
		CustomerDTO customerDTO = new CustomerDTO();
		modelMapper.map(entity, customerDTO);
		return customerDTO;
	}

	public Customer convertCustomerDTOtoEntity(CustomerDTO customerDTO) {
		Customer entity = new Customer();
		modelMapper.map(customerDTO, entity);
		return entity;
	}
	
	public Customer convertCustomerDTOtoEntity(CustomerDTO customerDTO,Customer entity) {
		modelMapper.map(customerDTO, entity);
		return entity;
	}

	public static CustomerDetailDTO mapToCustomerDetailsDTO(Customer customer, CustomerDetailDTO customerDetailsDto) {
		customerDetailsDto.setName(customer.getName());
		customerDetailsDto.setEmail(customer.getEmail());
		customerDetailsDto.setMobileNumber(customer.getMobileNumber());
		return customerDetailsDto;
	}

}
