package com.mf.productservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import com.mf.productservice.dto.request.AuthRequest;
import com.mf.productservice.dto.response.AuthResponse;
import com.mf.productservice.service.AuthService;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class TokenControllerTest {
	
	@InjectMocks
	private TokenController tokenController;
	
	@Mock
	private AuthService authService;
	
	@Mock
	RuntimeException ex;
	
	private AuthResponse authResponse;
	private AuthRequest authRequest;

	@Test
	void generateTokenTest() {
		authResponse = new AuthResponse("token");
		authRequest = new AuthRequest("juan@email", "123");	
		when(authService.generateToken(any(AuthRequest.class))).thenReturn(authResponse);
		assertEquals(HttpStatus.OK, tokenController.generateToken(authRequest).getStatusCode());
	}
	
	@Test
	void fallBackGenerateTokenTest(final CapturedOutput capturedOutput) {
		ReflectionTestUtils.invokeMethod(tokenController, "fallBackGenerateToken", ex);
		assertThat(capturedOutput).contains("FallbackToken is executed because user service is down ");
	}

}
