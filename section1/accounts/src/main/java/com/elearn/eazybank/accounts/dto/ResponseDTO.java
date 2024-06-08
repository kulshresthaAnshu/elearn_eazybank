package com.elearn.eazybank.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseDTO {
	private String statusCode;
	private String statusMsg;
}
