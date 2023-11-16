package com.mf.user.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.mf.user.exceptions.ValidationException;

@ExtendWith(MockitoExtension.class)
class ControllerAdviceUsersTest {
	
	@InjectMocks
	ControllerAdviceUsers controllerAdviceUsers;

	@Test
	void validationUsersTest() {
		ValidationException validation = new ValidationException("Error");
		assertEquals(HttpStatus.BAD_REQUEST, controllerAdviceUsers.validationUsers(validation).getStatusCode());
	}

}
