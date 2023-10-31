package com.mf.cartservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mf.cartservice.dto.response.Response;
import com.mf.cartservice.exception.ValidationException;

@RestControllerAdvice
public class CartControllerAdvice {

	@ExceptionHandler(value = ValidationException.class)
	public ResponseEntity<Response> validationCart(final ValidationException validation){
		Response response =  new Response(validation.getMessage(), null);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
