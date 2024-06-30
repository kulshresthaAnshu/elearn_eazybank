package com.elearn.eazybank.loans.dto;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "loans")
@Getter
@Setter
public class LoansContactInfoDTO {
	private String message;
	private Map<String, String> contactDetails;
	private List<String> onCallSupport;
}
