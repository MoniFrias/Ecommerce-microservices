package com.mf.user.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mf.user.dto.request.AddressRequestDTO;
import com.mf.user.dto.request.UserRequestDTO;
import com.mf.user.dto.response.AddressResponseDTO;
import com.mf.user.dto.response.Response;
import com.mf.user.entity.Role;
import com.mf.user.entity.User;
import com.mf.user.service.UserService;

@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UserControllerTest {
		
	@MockBean
	private UserService userService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private UserRequestDTO userRequestDTO;
	private AddressRequestDTO addressRequestDTO;
	private User user;
	private Response response;

	@BeforeEach
	void setup() {
		userRequestDTO = 
				UserRequestDTO.builder().firstname("juan").lastname("perez").email("juan@email").password("123").build();
		addressRequestDTO = 
				AddressRequestDTO.builder().addressline("Av 12").city("Canada").zipcode("1234")
				.state("Ccanada").country("Canada").userEmail("juan@email").build();
		user = new User(1L, "juan", "lopez", "juan@email", "122", Role.USER, new ArrayList<>());
		response =  new Response(true, "User was created successfully");
	}
	
	@Test
	void addAdminTest() throws Exception {				
		when(userService.createAdmin(any(UserRequestDTO.class))).thenReturn(response);
		
		ResultActions result = mockMvc.perform(post("/api/v1/user/addAdmin")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userRequestDTO)));
		
		result.andDo(print())
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.message", is(response.getMessage())));
		
	}
	
	@Test
	void updateAdminTest() throws JsonProcessingException, Exception {
		when(userService.updateAdmin(any(userRequestDTO.getClass()), anyLong())).thenReturn(response);
		ResultActions result = mockMvc.perform(put("/api/v1/user/updateAdmin/{idAdmin}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRequestDTO)));
		result.andDo(print()).andExpect(status().isOk());
	}

	@Test
	void deleteAdminTest() throws Exception {
		when(userService.deleteAdmin(anyLong())).thenReturn(response);
		mockMvc.perform(delete("/api/v1/user/deleteAdmin/{idAdmin}", 1L)).andExpect(status().isOk())
		.andExpect(jsonPath("$.result", is(response.isResult())));		
	}
	
	@Test
	void getUserTest() throws Exception {		
		when(userService.getUser(anyString())).thenReturn(Optional.of(user));
		mockMvc.perform(get("/api/v1/user/getUser/{email}", user.getEmail()))
		.andExpect(jsonPath("$.firstname", is(user.getFirstname())));		
	}
	
	@Test
	void addCustomer() throws Exception {
		when(userService.createCustomer(any(UserRequestDTO.class))).thenReturn(response);
		ResultActions result = mockMvc.perform(post("/api/v1/user/addCustomer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRequestDTO)));
		result.andDo(print()).andExpect(status().isCreated());
	}
	
	@Test
	void addAddressTest() throws Exception {
		when(userService.addAddress(any(AddressRequestDTO.class))).thenReturn(response);
		ResultActions result = mockMvc.perform(post("/api/v1/user/addAddress")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRequestDTO)));
		result.andDo(print()).andExpect(status().isCreated());
	}
	
	@Test
	void getUserByIdTest() throws Exception {		
		when(userService.getUserById(anyLong())).thenReturn(user);
		mockMvc.perform(get("/api/v1/user/getUserById/{userid}", 1L))
		.andExpect(jsonPath("$.firstname", is(user.getFirstname())));		
	}
	
	@Test
	void getAllAddresByUserTest() throws Exception {
		AddressResponseDTO addressResponseDTO2 = AddressResponseDTO
				.builder().addressline("Av 12").city("Canada").zipcode("1234")
				.state("Ccanada").country("Canada").build();
		List<AddressResponseDTO> listAddress = new ArrayList<>(Arrays.asList(addressResponseDTO2));
		
		when(userService.getAllAddresByUser(anyLong())).thenReturn(listAddress);
		mockMvc.perform(get("/api/v1/user/getAllAddresByUser/{userid}", 1L))
		.andExpect(jsonPath("$[0].city", is("Canada")));	
	}
	
	@Test
	void updateCustomerTest() throws JsonProcessingException, Exception {
		when(userService.updateCustomer(any(UserRequestDTO.class), anyLong())).thenReturn(response);
		ResultActions result = mockMvc.perform(put("/api/v1/user/updateCustomer/{idcustomer}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRequestDTO)));
		result.andDo(print()).andExpect(status().isOk());
	}

	@Test
	void updateAddressTest() throws JsonProcessingException, Exception {
		when(userService.updateAddress(any(AddressRequestDTO.class), anyLong())).thenReturn(response);
		ResultActions result = mockMvc.perform(put("/api/v1/user/updateAddress/{idaddress}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(addressRequestDTO)));
		result.andDo(print()).andExpect(status().isOk());
	}
	
	@Test
	void deleteCustomerTest() throws Exception {
		when(userService.deleteCustomer(anyLong())).thenReturn(response);
		mockMvc.perform(delete("/api/v1/user/deleteCustomer/{id}", 1L)).andExpect(jsonPath("$.message", is(response.getMessage())));
	}
	
	@Test
	void deleteAddressTest() throws Exception {
		when(userService.deleteAddress(anyLong(), anyLong())).thenReturn(response);
		mockMvc.perform(delete("/api/v1/user/deleteAddress/{iduser}/{idaddress}", 1L, 1L)).andExpect(status().isOk());

	}
}
