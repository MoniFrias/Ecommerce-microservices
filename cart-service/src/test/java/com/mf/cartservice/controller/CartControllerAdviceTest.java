package com.mf.cartservice.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.mf.cartservice.exception.ValidationException;

@ExtendWith(MockitoExtension.class)
class CartControllerAdviceTest {
	
	@InjectMocks
	private CartControllerAdvice cartControllerAdvice;

	@Test
	void validationCartTest() {
		assertEquals(HttpStatus.BAD_REQUEST, cartControllerAdvice.validationCart(new ValidationException("Error")).getStatusCode());
	}

}
