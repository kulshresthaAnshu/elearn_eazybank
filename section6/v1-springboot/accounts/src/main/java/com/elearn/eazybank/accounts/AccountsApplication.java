package com.elearn.eazybank.accounts;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.elearn.eazybank.accounts.dto.AccountsContactInfoDTO;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@EnableJpaAuditing
@OpenAPIDefinition(info = @Info(title = "Account Microservice Rest API Documentation", description = "Eazybank Account Microservice Rest API Documentation", version = "v1", contact = @Contact(name = "Anshu K", email = ""), license = @License(name = "Apache 2.0")), externalDocs = @ExternalDocumentation(description = "Eazybank Account Microservice Rest API Documentation", url = "https://www.udemy.com/course/master-microservices-with-spring-docker-kubernetes/"))
@EnableConfigurationProperties(value= AccountsContactInfoDTO.class)
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
