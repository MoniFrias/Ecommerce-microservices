package com.mf.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mf.user.dto.Mapper;
import com.mf.user.dto.request.AddressRequestDTO;
import com.mf.user.dto.request.UserRequestDTO;
import com.mf.user.entity.Address;
import com.mf.user.entity.Role;
import com.mf.user.entity.User;
import com.mf.user.exceptions.ValidationException;
import com.mf.user.repository.AddressRepository;
import com.mf.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	
	@InjectMocks
	UserService userService;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	AddressRepository addressRepository;
	
	@Mock
	Mapper mapper;
	
	private UserRequestDTO userRequestDTO;
	private AddressRequestDTO addressRequestDTO;
	private User user;
	private Address address;

	@BeforeEach
	void setup() {
		userRequestDTO = UserRequestDTO.builder().firstname("juan").lastname("perez").email("juan@email").password("123").build();
		user = new User(1L, "juan", "lopez", "juan@email", "122", Role.USER, new ArrayList<>());
		addressRequestDTO = 
				AddressRequestDTO.builder().addressline("Av 12").city("Canada").zipcode("1234")
				.state("Ccanada").country("Canada").userEmail("juan@email").build();
    	address = new Address(1L, "Av. 155", "Vancouver", "E122", "Vancouver", "Canada", user);

	}
	
	@Test
	void createAdminTest_existingAdminNonNull() {
		when(userRepository.findUserByEmail(userRequestDTO.getEmail())).thenReturn(user);
		assertThrows(ValidationException.class, ()-> {
			userService.createAdmin(userRequestDTO);
		});
		verify(userRepository, never()).save(any(User.class));
	}
	
	@Test
	void createAdminTest_existingAdminIsNull() {		
		when(userRepository.findUserByEmail(userRequestDTO.getEmail())).thenReturn(null);
		when(mapper.userRequestToUser(userRequestDTO)).thenReturn(user);
		assertEquals("User was created successfully", userService.createAdmin(userRequestDTO).getMessage());
	}
	
	@Test
	void updateAdminTest_ExistingAdmin() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(mapper.updateUser(user, userRequestDTO)).thenReturn(user);
		assertEquals("User was updated successfully", userService.updateAdmin(userRequestDTO, 1L).getMessage());		
	}
	
	@Test
	void updateAdminTest_NotExistingAdmin() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(ValidationException.class, ()-> {
			userService.updateAdmin(userRequestDTO, 1L);
		});
		verify(userRepository, never()).save(any(User.class));
	}
	
	@Test
	void deleteAdminTest_ExistingAdmin() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		assertTrue(userService.deleteAdmin(1L).isResult());
	}
	
	@Test
	void deleteAdminTest_NotExistingAdmin() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(ValidationException.class, () -> {
			userService.deleteAdmin(1L);
		});
		verify(userRepository, never()).deleteById(1L);
	}
	
    @Test
    void getUserTest() {
    	given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
    	Optional<User> user = userService.getUser(anyString());
    	assertTrue(user.isPresent());    	
    }
    
    @Test
    void createCustomerTest_nonNull() {
    	when(userRepository.findUserByEmail(anyString())).thenReturn(user);
    	assertThrows(ValidationException.class, () -> {
    		userService.createCustomer(userRequestDTO);
    	});
    	verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void createCustomerTest_isNull() throws Exception {
    	given(mapper.userRequestToUser(userRequestDTO)).willReturn(user);
    	assertTrue(userService.createCustomer(userRequestDTO).isResult());
    }
    
    @Test
    void addAddressTest_nonNullCustomer_IsNullAddress() throws Exception {    	
    	Address address = new Address(1L, "Av. 155", "Vancouver", "E122", "Vancouver", "Canada", user);
		    	
    	given(userRepository.findUserByEmail(addressRequestDTO.getUserEmail())).willReturn(user);
    	given(addressRepository.findAddressByAddressLineAndZipcode(addressRequestDTO.getAddressline(), addressRequestDTO.getZipcode(), 1L)).willReturn(null);
    	when(mapper.addressRequestToAddress(addressRequestDTO, user)).thenReturn(address);
    	assertTrue(userService.addAddress(addressRequestDTO).isResult());
    }
    
    @Test
    void addAddressTest_nonNullCustomer_NonNullAddress() {
    	given(userRepository.findUserByEmail(addressRequestDTO.getUserEmail())).willReturn(user);
    	given(addressRepository.findAddressByAddressLineAndZipcode(addressRequestDTO.getAddressline(), addressRequestDTO.getZipcode(), 1L)).willReturn(address);
    	assertThrows(ValidationException.class, () -> {
    		userService.addAddress(addressRequestDTO);
    	});
    	verify(addressRepository, never()).save(any(Address.class));
    }
    
    @Test
    void addAddressTest_isNullCustomer() {
    	when(userRepository.findUserByEmail(addressRequestDTO.getUserEmail())).thenReturn(null);
    	assertThrows(ValidationException.class, () -> {
    		userService.addAddress(addressRequestDTO);
    	});
    	verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void getUserByIdTest() {
    	given(userRepository.findById(1L)).willReturn(Optional.of(user));
    	assertTrue(Objects.nonNull(userService.getUserById(1L)));
    }
    
    @Test
    void getUserByIdTest_throws() {
    	given(userRepository.findById(1L)).willReturn(Optional.empty());
    	assertThrows(ValidationException.class, ()-> {
    		userService.getUserById(1L);
    	});
    }
    
    @Test
    void getAllAddresByUserTest() {
    	List<Address> listAddress = new ArrayList<>();
    	listAddress.add(address);
		given(addressRepository.findAddressListByIduser(1L)).willReturn(listAddress);
		assertFalse(userService.getAllAddresByUser(1L).isEmpty());
    }

    @Test
    void getAllAddresByUserTest_Empty() {
		given(addressRepository.findAddressListByIduser(1L)).willReturn(new ArrayList<>());
		assertThrows(ValidationException.class, ()-> {
			userService.getAllAddresByUser(1L);
		});
    }
    
    @Test
    void updateCustomerTest() {
    	given(userRepository.findById(1L)).willReturn(Optional.of(user));
    	given(mapper.updateUser(user, userRequestDTO)).willReturn(user);
    	assertTrue(userService.updateCustomer(userRequestDTO, 1L).isResult());
    }
    
    @Test
    void updateCustomerTest_Throws() {
    	given(userRepository.findById(1L)).willReturn(Optional.empty());
    	assertThrows(ValidationException.class, () -> {
    		userService.updateCustomer(userRequestDTO, 1L);
    	});
    	verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void updateAddressTest_nonNull() {
    	given(userRepository.findUserByEmail(addressRequestDTO.getUserEmail())).willReturn(user);
    	given(addressRepository.findById(1L)).willReturn(Optional.of(address));
    	given(mapper.updateAddress(address, addressRequestDTO)).willReturn(address);
    	assertTrue(userService.updateAddress(addressRequestDTO, 1L).isResult());
    }
    
    @Test
    void updateAddressTest_isNull() {
    	given(userRepository.findUserByEmail(addressRequestDTO.getUserEmail())).willReturn(null);
    	assertThrows(ValidationException.class, () -> {
    		userService.updateAddress(addressRequestDTO, 1L);
    	});
    	verify(addressRepository, never()).save(any(Address.class));
    }
    
    @Test
    void deleteCustomerTest() {
    	given(userRepository.findById(1L)).willReturn(Optional.of(user));
    	assertEquals("Customer was successfully removed", userService.deleteCustomer(1L).getMessage());
    }
    
    @Test
    void deleteCustomerTest_Throws() {
    	given(userRepository.findById(1L)).willReturn(Optional.empty());
    	assertThrows(ValidationException.class, () -> {
    		userService.deleteCustomer(1L);
    	});
    	verify(userRepository, never()).deleteById(1L);
    }
    
    @Test
    void deleteAddressTest() {
    	given(userRepository.findById(1L)).willReturn(Optional.of(user));
    	given(addressRepository.findById(1L)).willReturn(Optional.of(address));
    	assertTrue(userService.deleteAddress(1L, 1L).isResult());
    }
    
    @Test
    void deleteAddressTest_addressNull_Throws() {
    	given(userRepository.findById(1L)).willReturn(Optional.of(user));
    	given(addressRepository.findById(1L)).willReturn(Optional.empty());
    	assertThrows(ValidationException.class, () -> {
    		userService.deleteAddress(1L, 1L);
    	});
    	verify(userRepository, never()).deleteById(1L);
    }
    
    @Test
    void deleteAddressTest_userNull_Throws() {
    	given(userRepository.findById(1L)).willReturn(Optional.empty());
    	assertThrows(ValidationException.class, () -> {
    		userService.deleteAddress(1L, 1L);
    	});
    	verify(userRepository, never()).deleteById(1L);
    }
}
