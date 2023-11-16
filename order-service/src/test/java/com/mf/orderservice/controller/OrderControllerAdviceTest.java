package com.mf.orderservice.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.mf.orderservice.exception.ValidationException;

@ExtendWith(MockitoExtension.class)
class OrderControllerAdviceTest {
	
	@InjectMocks
	private OrderControllerAdvice orderControllerAdvice;

	@Test
	void validationOrderTest() {
		ValidationException validation =  new ValidationException("Error");
		assertEquals(HttpStatus.BAD_REQUEST, orderControllerAdvice.validationOrder(validation).getStatusCode());
	}

}
