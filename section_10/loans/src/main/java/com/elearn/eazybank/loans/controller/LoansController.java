package com.elearn.eazybank.loans.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.elearn.eazybank.loans.constants.LoanConstants;
import com.elearn.eazybank.loans.dto.ErrorResponseDTO;
import com.elearn.eazybank.loans.dto.LoanDTO;
import com.elearn.eazybank.loans.dto.LoansContactInfoDTO;
import com.elearn.eazybank.loans.dto.ResponseDTO;
import com.elearn.eazybank.loans.service.LoanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

/**
 * @author Anshu K
 */

@Tag(name = "CRUD REST APIs for Loans in EazyBank", description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE loan details")
@RestController
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
public class LoansController {

	private static final Logger logger= LoggerFactory.getLogger(LoansController.class);
	@Autowired
	private LoanService loanService;
	
	@Value("${build.version}")
	private String buildVerion;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private LoansContactInfoDTO loansContactInfoDTO;

	@Operation(summary = "Create Loan REST API", description = "REST API to create new loan inside EazyBank")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
		@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@PostMapping("/create")
	public ResponseEntity<ResponseDTO> createLoan(
			@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
		loanService.createLoan(mobileNumber);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDTO(LoanConstants.STATUS_201, LoanConstants.MESSAGE_201));
	}

	@Operation(summary = "Fetch Loan Details REST API", description = "REST API to fetch loan details based on a mobile number")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
		@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
		@GetMapping("/fetch")
		public ResponseEntity<LoanDTO> fetchLoanDetails(@RequestHeader("eazybank-correlation-id")
															String correlationId,
				@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
		logger.debug("eazyBank-correlation-id found: {} ", correlationId);
		LoanDTO loansDto = loanService.fetchLoan(mobileNumber);
		return ResponseEntity.status(HttpStatus.OK).body(loansDto);
	}

	@Operation(summary = "Update Loan Details REST API", description = "REST API to update loan details based on a loan number")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
		@ApiResponse(responseCode = "417", description = "Expectation Failed"),
		@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@PutMapping("/update")
	public ResponseEntity<ResponseDTO> updateLoanDetails(@Valid @RequestBody LoanDTO loansDto) {
		boolean isUpdated = loanService.updateLoan(loansDto);
		if (isUpdated) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseDTO(LoanConstants.STATUS_200, LoanConstants.MESSAGE_200));
		} else {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(new ResponseDTO(LoanConstants.STATUS_417, LoanConstants.MESSAGE_417_UPDATE));
		}
	}

	@Operation(summary = "Delete Loan Details REST API", description = "REST API to delete Loan details based on a mobile number")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
		@ApiResponse(responseCode = "417", description = "Expectation Failed"),
		@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@DeleteMapping("/delete")
	public ResponseEntity<ResponseDTO> deleteLoanDetails(
			@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
		boolean isDeleted = loanService.deleteLoan(mobileNumber);
		if (isDeleted) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseDTO(LoanConstants.STATUS_200, LoanConstants.MESSAGE_200));
		} else {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(new ResponseDTO(LoanConstants.STATUS_417, LoanConstants.MESSAGE_417_DELETE));
		}
	}
	@Operation(summary = "GET API for  Build Info for Loans MS", description = "This API gives the build info for Loans MS of eazy bank")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Http Status OK"),
		@ApiResponse(responseCode = "500", description = "Http Status Internal server Error") })
	@GetMapping("/build-info")
	public ResponseEntity<String> getBuildInfo() {
		return ResponseEntity.status(HttpStatus.OK).body(buildVerion);
	}
	
	@Operation(summary = "GET API for  Environment Info for Loans MS", description = "This API gives the Env info for Loans MS of eazy bank")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Http Status OK"),
		@ApiResponse(responseCode = "500", description = "Http Status Internal server Error") })
	@GetMapping("/java-version")
	public ResponseEntity<String> getEnvInfo() {
//		return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("JAVA_HOME"));
		return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("MAVEN_HOME"));
	}
	
	@Operation(summary = "GET API for  Contact Info for Loans MS", description = "This API gives the Contact info for Loans MS of eazy bank")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Http Status OK"),
		@ApiResponse(responseCode = "500", description = "Http Status Internal server Error") })
	@GetMapping("/contact-info")
	public ResponseEntity<LoansContactInfoDTO> getContactInfo() {
			return ResponseEntity.status(HttpStatus.OK).body(loansContactInfoDTO);
//		return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("MAVEN_HOME"));
	}
}