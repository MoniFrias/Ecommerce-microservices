package com.mf.user.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mf.user.dto.request.AddressRequestDTO;
import com.mf.user.dto.request.UserRequestDTO;
import com.mf.user.dto.response.AddressResponseDTO;
import com.mf.user.dto.response.Response;
import com.mf.user.entity.User;
import com.mf.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

private final UserService userService;

	
	@PostMapping("/addAdmin")
	public ResponseEntity<Response> addAdmin(@RequestBody UserRequestDTO admin) throws Exception{
		Response response = userService.createAdmin(admin);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/updateAdmin/{idAdmin}")
	public ResponseEntity<Response> updateAdmin(@RequestBody UserRequestDTO admin, @PathVariable Long idAdmin){
		Response response = userService.updateAdmin(admin, idAdmin);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteAdmin/{idAdmin}")
	public ResponseEntity<Response> deleteAdmin(@PathVariable Long idAdmin){
		Response response = userService.deleteAdmin(idAdmin);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/getUser/{email}")
	public Optional<User> getUser(@PathVariable String email){
		return userService.getUser(email);
	}
	
	@PostMapping("/addCustomer")
	public ResponseEntity<Response> addCustomer(@RequestBody UserRequestDTO customer) throws Exception{
		Response response = userService.createCustomer(customer);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/addAddress")
	public ResponseEntity<Response> addAddress(@RequestBody AddressRequestDTO address) throws Exception{
		Response response = userService.addAddress(address);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/getUserById/{userid}")
	public User getUserById(@PathVariable Long userid) {
		return userService.getUserById(userid);
	}
	
	@GetMapping("/getAllAddresByUser/{userid}")
	public List<AddressResponseDTO> getAllAddresByUser(@PathVariable Long userid) {
		return userService.getAllAddresByUser(userid);
	}
	
	@PutMapping("/updateCustomer/{idcustomer}")
	public ResponseEntity<Response> updateCustomer(@RequestBody UserRequestDTO customer, @PathVariable Long idcustomer){
		Response response = userService.updateCustomer(customer, idcustomer);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping("/updateAddress/{idaddress}")
	public ResponseEntity<Response> updateAddress(@RequestBody AddressRequestDTO address, @PathVariable Long idaddress){
		Response response = userService.updateAddress(address, idaddress);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteCustomer/{id}")
	public ResponseEntity<Response> deleteCustomer(@PathVariable Long id){
		Response response = userService.deleteCustomer(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteAddress/{iduser}/{idaddress}")
	public ResponseEntity<Response> deleteAddress(@PathVariable Long iduser, @PathVariable Long idaddress){
		Response response = userService.deleteAddress(iduser, idaddress);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
