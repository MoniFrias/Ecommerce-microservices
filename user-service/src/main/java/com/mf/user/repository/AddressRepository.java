package com.mf.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mf.user.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{

	@Query(nativeQuery = true, value = "SELECT * FROM Address a WHERE a.addressLine = ?1 AND a.zipcode = ?2 AND a.iduser = ?3")
	Address findAddressByAddressLineAndZipcode(String addressLine, String zipcode, Long iduser);

	@Query(nativeQuery = true, value = "SELECT* FROM Address a WHERE a.iduser = ?1")
	List<Address> findAddressListByIduser(Long userid);

}
