package com.mf.cartservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mf.cartservice.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{

}
