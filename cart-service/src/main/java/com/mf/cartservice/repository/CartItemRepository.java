package com.mf.cartservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mf.cartservice.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{

	@Query(nativeQuery = true, value = "SELECT * FROM Cart_item c WHERE c.idcart = ?1 AND c.idproduct = ?2")
	List<CartItem> findByIdCartAndIdproduct(Long idcart, Long idproduct);

	@Query(nativeQuery = true, value = "SELECT * FROM Cart_item c WHERE c.idcart = ?1")
	List<CartItem> findAllbyIdcart(Long idcart);
	
	@Query(nativeQuery = true, value = "SELECT * FROM Cart_item c WHERE c.idcart = ?1 AND c.ordernumber = ?2")
	List<CartItem> findAllItemsToOrder(Long idcart, String status);

	@Query(nativeQuery = true, value = "SELECT * FROM Cart_item c WHERE c.idcart = ?1")
	List<CartItem> findByIdcart(Long idcart);

	@Query(nativeQuery = true, value = "DELETE FROM Cart_item c WHERE c.idcart = ?1")
	void deleteAllByIdcart(Long idcart);
}
