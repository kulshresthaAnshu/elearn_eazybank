package com.elearn.eazybank.loans.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(name = "ErrorResponse", description = "Schema to hold ErrorResponse details")
public class ErrorResponseDTO {
	@Schema(description = "Schema to hold ErrorResponse apiPath")
	private String apiPath;
	@Schema(description = "Schema to hold ErrorResponse errorCode")
	private HttpStatus errorCode;
	@Schema(description = "Schema to hold ErrorResponse errorMsg")
	private String errorMsg;
	@Schema(description = "Schema to hold ErrorResponse errorTime")
	private LocalDateTime errorTime;
}
