package com.mf.productservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mf.productservice.dto.request.AuthRequest;
import com.mf.productservice.dto.response.AuthResponse;
import com.mf.productservice.service.authService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class TokenController {

	private final authService authService;
	private Logger logger = LoggerFactory.getLogger(TokenController.class);
	
	@CircuitBreaker(name = "generateToken", fallbackMethod = "fallBackGenerateToken")
	@PostMapping("/generateToken")
	public ResponseEntity<AuthResponse> generateToken(@RequestBody AuthRequest request) {
		return ResponseEntity.ok(authService.generateToken(request));
	}
	
	private ResponseEntity<String> fallBackGenerateToken(RuntimeException ex){
		logger.info("Fallback is executed because user service is down ", ex.getMessage());
		return new ResponseEntity<>("Oops! Something went wrong, please try again later!", HttpStatus.OK);
	}
}
