package com.mf.user.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mf.user.dto.Mapper;
import com.mf.user.dto.request.AddressRequestDTO;
import com.mf.user.dto.request.UserRequestDTO;
import com.mf.user.dto.response.AddressResponseDTO;
import com.mf.user.dto.response.Response;
import com.mf.user.entity.Address;
import com.mf.user.entity.Role;
import com.mf.user.entity.User;
import com.mf.user.exceptions.ValidationException;
import com.mf.user.repository.AddressRepository;
import com.mf.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final AddressRepository addressRepository;
	private final Mapper mapper;

	public Response createAdmin(UserRequestDTO admin) {
		User existingAdmin = userRepository.findUserByEmail(admin.getEmail());
		if (Objects.isNull(existingAdmin)) {
			User mapperUser = mapper.userRequestToUser(admin);
			mapperUser.setRole(Role.ADMIN);
			userRepository.save(mapperUser);
			return new Response(true, "User was created successfully");
		}
		throw new ValidationException("There is already a user with that email");
	}

	public Response updateAdmin(UserRequestDTO admin, Long idAdmin) {
		User existingAdmin = userRepository.findById(idAdmin)
				.orElseThrow(() -> new ValidationException("User with id: " + idAdmin + " does not exists"));
		User updatedUser = mapper.updateUser(existingAdmin, admin);
		userRepository.save(updatedUser);
		return new Response(true, "User was updated successfully");
	}

	public Response deleteAdmin(Long idAdmin) {
		User existingAdmin = userRepository.findById(idAdmin)
				.orElseThrow(() -> new ValidationException("User with id: " + idAdmin + " does not exists"));
		userRepository.deleteById(existingAdmin.getIduser());
		return new Response(true, "User was removed successfully");
	}

	public Optional<User> getUser(String email) {
		return userRepository.findByEmail(email);
	}

	public Response createCustomer(UserRequestDTO customer) throws Exception {
		User existingCustomer = userRepository.findUserByEmail(customer.getEmail());
		if (Objects.nonNull(existingCustomer)) {
			throw new ValidationException("There is already a user with that email");
		}
		User mapperUser = mapper.userRequestToUser(customer);
		mapperUser.setRole(Role.USER);
		userRepository.save(mapperUser);
		return new Response(true, "User was created successfully");
	}

	public Response addAddress(AddressRequestDTO addressRequest) throws Exception {
		Address mapperAddress = new Address();
		User existingCustomer = userRepository.findUserByEmail(addressRequest.getUserEmail());

		if (Objects.nonNull(existingCustomer)) {

			Address existingAddress = addressRepository.findAddressByAddressLineAndZipcode(
					addressRequest.getAddressline(), addressRequest.getZipcode(), existingCustomer.getIduser());

			if (Objects.isNull(existingAddress)) {
				mapperAddress = mapper.addressRequestToAddress(addressRequest, existingCustomer);
				addressRepository.save(mapperAddress);
				return new Response(true, "Address was created successfully");
			}
			throw new ValidationException("The address already exists for the user");

		}
		throw new ValidationException("User does not exists");
	}

	public User getUserById(Long userid) {
		return userRepository.findById(userid)
				.orElseThrow(() -> new ValidationException("User with ID: " + userid + " does not exists"));
	}

	public List<AddressResponseDTO> getAllAddresByUser(Long userid) {
		List<AddressResponseDTO> addressList = addressRepository.findAddressListByIduser(userid).stream()
				.map(address -> {
					AddressResponseDTO response = mapper.addressToAddressResponse(address);
					return response;
				}).collect(Collectors.toList());

		if (addressList.isEmpty()) {
			throw new ValidationException("There are not address for that user");
		}
		return addressList;
	}

	public Response updateCustomer(UserRequestDTO customerRequest, Long idcustomer) {
		User existingCustomer = userRepository.findById(idcustomer)
				.orElseThrow(() -> new ValidationException("User with ID: " + idcustomer + " does not exists"));

		User updateUser = mapper.updateUser(existingCustomer, customerRequest);
		userRepository.save(updateUser);
		return new Response(true, "User was updated successfully");
	}

	public Response updateAddress(AddressRequestDTO address, Long idaddress) {
		User existingCustomer = userRepository.findUserByEmail(address.getUserEmail());
		if (Objects.nonNull(existingCustomer)) {
			Address existingAddress = addressRepository.findById(idaddress)
					.orElseThrow(() -> new ValidationException("Address with ID: " + idaddress + " does not exists"));
			Address updateAddress = mapper.updateAddress(existingAddress, address);
			addressRepository.save(updateAddress);
			return new Response(true, "Address was updated successfully");
		}
		throw new ValidationException("User with that email not exists");
	}

	public Response deleteCustomer(Long id) {
		Response response = new Response();
		userRepository.findById(id).map(customer -> {
			userRepository.deleteById(customer.getIduser());
			response.setMessage("Customer was successfully removed");
			return response;
		}).orElseThrow(() -> new ValidationException("User with that id: " + id + " does not exists"));
		return response;
	}

	public Response deleteAddress(Long iduser, Long idaddress) {
		Response response = new Response();
		userRepository.findById(iduser).map(customer -> {
			Address existingAddress = addressRepository.findById(idaddress)
					.orElseThrow(() -> new ValidationException("Address with id " + idaddress + " does not exists"));
			addressRepository.deleteById(existingAddress.getIdaddress());
			response.setMessage("Address was successfully removed");
			return response;
		}).orElseThrow(() -> new ValidationException("User with that id: " + iduser + " does not exists"));
		return response;
	}
}
