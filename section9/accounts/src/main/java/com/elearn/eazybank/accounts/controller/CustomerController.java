package com.elearn.eazybank.accounts.controller;

import com.elearn.eazybank.accounts.dto.CustomerDetailDTO;
import com.elearn.eazybank.accounts.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "EazyBank Customer Microservice Rest CRUD API Details", description = "Account Microservice Rest API for fetch Customer Details")
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private CustomerService customerService;
    @Operation(summary = "GET API for Fetching Customer Details MS", description = "This API fetches customer details with cards accounts and loans")
    @ApiResponse(responseCode = "200", description = "Http Status Ok")
    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailDTO> fetchCustomerDetails(
            @RequestHeader("eazybank-correlation-id") String correlationId,
            @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber){
        logger.debug("eazyBank-correlation-id found: {} ", correlationId);
        CustomerDetailDTO customerDetailDTO = customerService.fetchCustomerDeatils(correlationId,mobileNumber);
        return new ResponseEntity<>(customerDetailDTO, HttpStatus.OK);
    }
}
