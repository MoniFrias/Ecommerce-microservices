package com.mf.cartservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mf.cartservice.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{

	@Query(nativeQuery = true, value = "SELECT * FROM Cart p WHERE p.iduser = ?1")
	Cart findByIdUser(Long iduser);

}
