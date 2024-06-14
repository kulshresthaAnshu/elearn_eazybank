package com.elearn.eazybank.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Schema(name = "Response", description = "Schema to hold Response details")
public class ResponseDTO {
	@Schema(description = "Schema to hold Response Status Code")
	private String statusCode;
	
	@Schema(description = "Schema to hold Response Status Msg")
	private String statusMsg;
}
