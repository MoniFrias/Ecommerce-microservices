package com.mf.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mf.user.entity.Role;
import com.mf.user.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	private User user;
	
	@BeforeEach
	void setup() {
		user = new User(1L, "juan", "lopez", "juan@email", "122", Role.USER, new ArrayList<>());
	}
	
	@Test
	void findUserByEmailTest() {
		User user1 = userRepository.findUserByEmail("luisgomez@email");
		assertThat(user1).isNotNull();
	}
	
	@Test
	void findByEmailTest() {
		Optional<User> user1 = userRepository.findByEmail("luisgomez@email");
		assertThat(user1).isPresent();
	}
	
	@Test
	void saveUserTest() {
		User user1 = userRepository.save(user);
		assertThat(user1.getFirstname()).isEqualTo(user.getFirstname());
		assertThat(user1.getIduser()).isGreaterThan(0);
	}
	
	@Test
	void findByIdTest() {
		Optional<User> user1 = userRepository.findById(1L);
		assertThat(user1).isPresent();
	}
	
	@Test
	void updateTest() {
		userRepository.save(user);
		User user1 = userRepository.findById(1L).get();
		user1.setLastname("gomez");
		User userUpdated = userRepository.save(user1);
		assertThat(userUpdated.getLastname()).isEqualTo("gomez");
	}
	
	@Test
	void deleteByIdTest() {
		userRepository.save(user);
		userRepository.deleteById(1L);
		Optional<User> user1 =userRepository.findById(user.getIduser());
		assertThat(user1).isEmpty();
	}

}
