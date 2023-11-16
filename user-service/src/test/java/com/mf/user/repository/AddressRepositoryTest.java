package com.mf.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mf.user.entity.Address;
import com.mf.user.entity.User;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AddressRepositoryTest {
	
	@Autowired
	private AddressRepository addressRepository;
	
	private Address address;
	
	@BeforeEach
	void setup() {
    	address = new Address(1L, "Av. 155", "Vancouver", "E122", "Vancouver", "Canada", new User());
	}

	@Test
	void findAddressByAddressLineAndZipcodeTest() {
		Address address1 = addressRepository.findAddressByAddressLineAndZipcode("E 26th Ave", "70654", 1L);
		assertThat(address1).isNotNull();
	}
	
	@Test
	void findAddressListByIduserTest() {
		List<Address> addresses = addressRepository.findAddressListByIduser( 1L);
		assertThat(addresses).isNotEmpty();
		assertThat(addresses.size()).isEqualTo(1);
	}
	
	@Test
	void saveAddresTest() {
		Address addressSave = addressRepository.save(address);
		assertThat(addressSave).isNotNull();
		assertThat(addressSave.getIdaddress()).isGreaterThan(0);
	}
	
	@Test
	void findByIdTest() {
		Optional<Address> address1 = addressRepository.findById(1L);
		assertThat(address1).isPresent();
	}
	
	@Test
	void updateTest() {
		addressRepository.save(address);
		Address addressSave = addressRepository.findById(1L).get();
		addressSave.setAddressline("E 26th Av");
		Address addressUpdated = addressRepository.save(addressSave);
		assertThat(addressUpdated.getAddressline()).isEqualTo("E 26th Av");
	}
	
	@Test
	void deleteAddressTest() {
		addressRepository.save(address);
		addressRepository.deleteById(address.getIdaddress());
		Optional<Address> address1 = addressRepository.findById(address.getIdaddress());
		assertThat(address1).isEmpty();
	}

}
