package com.elearn.eazybank.accounts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elearn.eazybank.accounts.constants.AccountConstant;
import com.elearn.eazybank.accounts.dd.CustomerDTO;
import com.elearn.eazybank.accounts.dd.ErrorResponseDTO;
import com.elearn.eazybank.accounts.dd.ResponseDTO;
import com.elearn.eazybank.accounts.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@Tag(name = "EazyBank Account Microservice Rest CRUD API Details", description = "Account Microservice Rest API Documentation for CREATE, READ,UPDATE,DELETE operations")
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class AccountsController {

	@Autowired
	private AccountService accountService;

	@Operation(summary = "GET API for verifying the staus of Account MS", description = "Account MS hello method to verify the UP status")
	@GetMapping("/sayHello")
	public String satHello() {
		return "sayHello from accounts";
	}

	@Operation(summary = "POST API for Creating Account MS", description = "This API creates a customer and account inside eazy bank")
	@ApiResponse(responseCode = "201", description = "Http Status Created")
	@PostMapping("/create")
	public ResponseEntity<ResponseDTO> createAccount(@RequestBody CustomerDTO customerDTO) {
		accountService.createAccount(customerDTO);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDTO(AccountConstant.STATUS_201, AccountConstant.MESSAGE_201));
	}

	@Operation(summary = "GET API for  Account MS", description = "This API fetches a customer and account inside eazy bank based on mobile number")
	@ApiResponse(responseCode = "200", description = "Http Status OK")
	@GetMapping("/fetch")
	public ResponseEntity<CustomerDTO> fetchAccountDetails(
			@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
		CustomerDTO customerDTO = accountService.fetchAccount(mobileNumber);
		return ResponseEntity.status(HttpStatus.OK).body(customerDTO);
	}

	@Operation(summary = "PUT API for  Account MS", description = "This API updates a customer details inside eazy bank")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Http Status OK"),
		@ApiResponse(responseCode = "500", description = "Http Status Internal server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@PutMapping("/update")
	public ResponseEntity<ResponseDTO> updateAccountDetails(@Valid @RequestBody CustomerDTO customerDto) {
		boolean isUpdated = accountService.updateAccount(customerDto);
		if (isUpdated) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseDTO(AccountConstant.STATUS_200, AccountConstant.MESSAGE_200));
		} else {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(new ResponseDTO(AccountConstant.STATUS_417, AccountConstant.MESSAGE_417_UPDATE));
		}
	}

	@Operation(summary = "DELETE API for  Account MS", description = "This API deletes a customer and account inside eazy bank")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Http Status OK"),
		@ApiResponse(responseCode = "500", description = "Http Status Internal server Error") })
	@DeleteMapping("/delete")
	public ResponseEntity<ResponseDTO> deleteAccountDetails(
			@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
		boolean isDeleted = accountService.deleteAccount(mobileNumber);
		if (isDeleted) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseDTO(AccountConstant.STATUS_200, AccountConstant.MESSAGE_200));
		} else {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(new ResponseDTO(AccountConstant.STATUS_417, AccountConstant.MESSAGE_417_DELETE));
		}
	}

}
