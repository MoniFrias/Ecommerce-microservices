package com.mf.productservice.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.mf.productservice.configuration.JwtUtils;
import com.mf.productservice.dto.request.AuthRequest;
import com.mf.productservice.feignClients.UserFeignClient;
import com.mf.productservice.model.Role;
import com.mf.productservice.model.User;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	
	@InjectMocks
	private AuthService authService;
	
	@Mock
	private UserFeignClient userFeignClient;
	
	@Mock
	private AuthenticationManager authenticationManager;
	
	@Mock
	private Authentication authentication;
	
	@Mock
	private JwtUtils jwtUtils;

	@Test
	void testGenerateToken() {
		
		User user= new User(1L, "juan", "lopez", "juan@email", "123", Role.USER);
		AuthRequest request = new AuthRequest("juan@email", "123");		
		Authentication authentication = mock(Authentication.class);
		authentication.setAuthenticated(true);
		
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
		when(userFeignClient.getUser(anyString())).thenReturn(Optional.of(user));
		when(jwtUtils.generateToken(user)).thenReturn("token");
		assertTrue(!authService.generateToken(request).getToken().isEmpty());
	}

}
