package com.mf.productservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mf.productservice.entity.Product;

@Repository
public interface RepositoryProduct extends JpaRepository<Product, Long>{

	Product findProductByProductname(String productname);

	@Query(nativeQuery = true, value = "SELECT * FROM Product p WHERE p.idsku = ?1")
	Product findProductByIdsku(String sku);

	List<Product> findProductsByBrandname(String brandName);
	
	Product findProductByIdproduct(Long id);

}
