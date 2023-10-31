package com.mf.user.dto;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mf.user.dto.request.AddressRequestDTO;
import com.mf.user.dto.request.UserRequestDTO;
import com.mf.user.dto.response.AddressResponseDTO;
import com.mf.user.entity.Address;
import com.mf.user.entity.User;

@Configuration
public class Mapper {

	private final PasswordEncoder passwordEncoder;
	
	public Mapper() {
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public User userRequestToUser(UserRequestDTO customer) {		
		User user = new User();
		user.setFirstname(customer.getFirstname());
		user.setLastname(customer.getLastname());
		user.setEmail(customer.getEmail());
		user.setPassword(passwordEncoder.encode(customer.getPassword()));
		return user;
	}
	
	public User updateUser(User existingUser, UserRequestDTO userRequest) {
		existingUser.setFirstname(userRequest.getFirstname());
		existingUser.setLastname(userRequest.getLastname());
		existingUser.setEmail(userRequest.getEmail());
		existingUser.setPassword(userRequest.getPassword());
		return existingUser;
	}

	public  Address addressRequestToAddress(AddressRequestDTO addressRequest, User existingUser) {
		Address address =  new Address();
		address.setAddressline(addressRequest.getAddressline());
		address.setCity(addressRequest.getCity());
		address.setZipcode(addressRequest.getZipcode());
		address.setState(addressRequest.getState());
		address.setCountry(addressRequest.getCountry());
		address.setUser(existingUser);
		return address;
	}

	public AddressResponseDTO addressToAddressResponse(Address addressResponse) {
		AddressResponseDTO address =  new AddressResponseDTO();
		address.setAddressline(addressResponse.getAddressline());
		address.setCity(addressResponse.getCity());
		address.setZipcode(addressResponse.getZipcode());
		address.setState(addressResponse.getState());
		address.setCountry(addressResponse.getCountry());
		return address;
	}

	public Address updateAddress(Address existingAddress, AddressRequestDTO addressRequest) {
		existingAddress.setAddressline(addressRequest.getAddressline());
		existingAddress.setCity(addressRequest.getCity());
		existingAddress.setZipcode(addressRequest.getZipcode());
		existingAddress.setState(addressRequest.getState());
		existingAddress.setCountry(addressRequest.getCountry());
		return existingAddress;
	}
}
