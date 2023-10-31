package com.mf.orderservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mf.orderservice.dto.response.Response;
import com.mf.orderservice.exception.ValidationException;

@RestControllerAdvice
public class OrderControllerAdvice {

	@ExceptionHandler(value = ValidationException.class)
	public ResponseEntity<Response> validationOrder(final ValidationException validation){
		Response response = new Response(validation.getMessage(), null);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
