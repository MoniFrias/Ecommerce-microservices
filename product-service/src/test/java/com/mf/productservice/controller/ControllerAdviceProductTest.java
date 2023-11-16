package com.mf.productservice.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.mf.productservice.exceptions.ValidationException;


@ExtendWith(MockitoExtension.class)
class ControllerAdviceProductTest {

	@InjectMocks
	ControllerAdviceProduct controllerAdviceProduct;
	
	@Test
	void validationProductTest() {
		ValidationException validation = new ValidationException("Error");
		assertEquals(HttpStatus.BAD_REQUEST, controllerAdviceProduct.validationProduct(validation).getStatusCode());
	}

}
