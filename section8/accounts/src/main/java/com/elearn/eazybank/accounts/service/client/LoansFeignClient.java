package com.elearn.eazybank.accounts.service.client;

import com.elearn.eazybank.accounts.dto.CardDTO;
import com.elearn.eazybank.accounts.dto.LoanDTO;
import jakarta.validation.constraints.Pattern;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("loans")
public interface LoansFeignClient {
    @GetMapping("/api/fetch")
    public ResponseEntity<LoanDTO> fetchLoanDetails(
            @RequestParam String mobileNumber);

}
