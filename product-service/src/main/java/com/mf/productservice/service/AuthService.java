package com.mf.productservice.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mf.productservice.configuration.JwtUtils;
import com.mf.productservice.dto.request.AuthRequest;
import com.mf.productservice.dto.response.AuthResponse;
import com.mf.productservice.feignClients.UserFeignClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final UserFeignClient userFeignClient;
	private final AuthenticationManager authenticationManeger;
	private final JwtUtils jwtUtils;

	public AuthResponse generateToken(AuthRequest request) {
		authenticationManeger.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		UserDetails user = userFeignClient.getUser(request.getEmail()).orElseThrow();
		return AuthResponse.builder().token(jwtUtils.generateToken(user)).build();
	}

}
