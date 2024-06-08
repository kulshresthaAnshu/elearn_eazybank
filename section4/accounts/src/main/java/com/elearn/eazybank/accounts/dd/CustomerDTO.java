package com.elearn.eazybank.accounts.dd;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name="Customer", description="Schema to hold Customer details")
public class CustomerDTO {
	
	@Schema(description="Eazy Bank Account Customer name")
	@NotEmpty(message = "name can not be null or empty")
	private String name;
	
	@Schema(description="Eazy Bank Account Customer email")
	@NotEmpty(message = "email can not be null or empty")
	@Email(message= "Email should be a valid value")
	private String email;
	
	@Schema(description="Eazy Bank Account Customer  mobile number")
	@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
	private String mobileNumber;
	
	@Schema(description="Eazy Bank Account Customer  account details")
	private AccountDTO accountDto;
}
