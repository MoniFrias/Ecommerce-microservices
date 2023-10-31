package com.mf.productservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mf.productservice.dto.request.AuthRequest;
import com.mf.productservice.dto.response.AuthResponse;
import com.mf.productservice.service.authService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class TokenController {

	private final authService authService;
	
	@PostMapping("/generateToken")
	public ResponseEntity<AuthResponse> generateToken(@RequestBody AuthRequest request) {
		return ResponseEntity.ok(authService.generateToken(request));
	}
}
