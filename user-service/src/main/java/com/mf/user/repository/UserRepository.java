package com.mf.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mf.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	User findUserByEmail(String email);

	Optional<User> findByEmail(String email);

}
