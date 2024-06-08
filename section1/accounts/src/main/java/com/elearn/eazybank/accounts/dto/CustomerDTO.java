package com.elearn.eazybank.accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerDTO {
	
	@NotEmpty(message = "name can not be null or empty")
	private String name;
	
	@NotEmpty(message = "email can not be null or empty")
	@Email(message= "Email should be a valid value")
	private String email;
	
	@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
	private String mobileNumber;
	private AccountDTO accountDto;
}
