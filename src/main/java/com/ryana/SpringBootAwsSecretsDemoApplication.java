package com.ryana;

import com.ryana.dto.AwsSecrets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@SpringBootApplication
public class SpringBootAwsSecretsDemoApplication {

	private final AwsSecrets awsSecrets;

	@GetMapping("/secrets")
	public AwsSecrets getSecrets() {
		return awsSecrets;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAwsSecretsDemoApplication.class, args);
	}

}
