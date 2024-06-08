package com.elearn.eazybank.accounts.dd;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name="Account", description="Schema to hold Customer details")
public class AccountDTO {
	@Schema(description="Eazy Bank Account number")
	@Pattern(regexp = "(^$|[0-9]{10})", message = "Account number must be 10 digits")
	private Long accountNumber;
	@Schema(description="Eazy Bank Account Type")
	private String accountType;
	@Schema(description="Eazy Bank Branch Address")
	private String branchAddress;

}
