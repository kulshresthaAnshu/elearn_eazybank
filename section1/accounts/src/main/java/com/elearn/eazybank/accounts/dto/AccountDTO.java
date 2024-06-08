package com.elearn.eazybank.accounts.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AccountDTO {
	@Pattern(regexp = "(^$|[0-9]{10})", message = "Account number must be 10 digits")
	private Long accountNumber;
	private String accountType;
	private String branchAddress;

}
