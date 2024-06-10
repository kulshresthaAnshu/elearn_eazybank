package com.elearn.eazybank.cards.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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

import com.elearn.eazybank.cards.constants.CardConstant;
import com.elearn.eazybank.cards.dto.CardDTO;
import com.elearn.eazybank.cards.dto.CardsContactInfoDTO;
import com.elearn.eazybank.cards.dto.ErrorResponseDTO;
import com.elearn.eazybank.cards.dto.ResponseDTO;
import com.elearn.eazybank.cards.service.CardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

@Tag(name = "CRUD REST APIs for Cards in EazyBank", description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE card details")
@RestController
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
public class CardController {

	private CardService cardService;
	
	@Autowired
	CardsContactInfoDTO cardsContactInfoDTO;
	
	@Autowired
	Environment env;
	
	@Value("${build.version}")
	String buildVersion;

	@Operation(summary = "Create Card REST API", description = "REST API to create new Card inside EazyBank")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
		@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@PostMapping("/create")
	public ResponseEntity<ResponseDTO> createCard(
			@Valid @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
		cardService.createCard(mobileNumber);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDTO(CardConstant.STATUS_201, CardConstant.MESSAGE_201));
	}

	@Operation(summary = "Fetch Card Details REST API", description = "REST API to fetch card details based on a mobile number")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
		@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@GetMapping("/fetch")
	public ResponseEntity<CardDTO> fetchCardDetails(
			@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
		CardDTO cardsDto = cardService.fetchCard(mobileNumber);
		return ResponseEntity.status(HttpStatus.OK).body(cardsDto);
	}

	@Operation(summary = "Update Card Details REST API", description = "REST API to update card details based on a card number")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
		@ApiResponse(responseCode = "417", description = "Expectation Failed"),
		@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@PutMapping("/update")
	public ResponseEntity<ResponseDTO> updateCardDetails(@Valid @RequestBody CardDTO cardsDto) {
		boolean isUpdated = cardService.updateCard(cardsDto);
		if (isUpdated) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseDTO(CardConstant.STATUS_200, CardConstant.MESSAGE_200));
		} else {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(new ResponseDTO(CardConstant.STATUS_417, CardConstant.MESSAGE_417_UPDATE));
		}
	}

	@Operation(summary = "Delete Card Details REST API", description = "REST API to delete Card details based on a mobile number")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
		@ApiResponse(responseCode = "417", description = "Expectation Failed"),
		@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@DeleteMapping("/delete")
	public ResponseEntity<ResponseDTO> deleteCardDetails(
			@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
		boolean isDeleted = cardService.deleteCard(mobileNumber);
		if (isDeleted) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseDTO(CardConstant.STATUS_200, CardConstant.MESSAGE_200));
		} else {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(new ResponseDTO(CardConstant.STATUS_417, CardConstant.MESSAGE_417_DELETE));
		}
	}
	
	@Operation(summary = "Fetch Build Info Details REST API", description = "REST API to fetch Build Info for Cards Microservice")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
		@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@GetMapping("/build-info")
	public ResponseEntity<String> getBuildInfo() {
		return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
	}

	@Operation(summary = "Fetch Java version Info Details REST API", description = "REST API to fetch Java version for Cards Microservice")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
		@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@GetMapping("/java-version")
	public ResponseEntity<String> getJavaVersion() {
		return ResponseEntity.status(HttpStatus.OK).body(env.getProperty("MAVEN_HOME"));
	}
	
	@Operation(summary = "Fetch Java version Info Details REST API", description = "REST API to fetch Contact Info for Cards Microservice")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
		@ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@GetMapping("/contact-info")
	public ResponseEntity<CardsContactInfoDTO> getContactInfo() {
		return ResponseEntity.status(HttpStatus.OK).body(cardsContactInfoDTO);
	}


}
