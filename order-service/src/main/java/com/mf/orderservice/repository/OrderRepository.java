package com.mf.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mf.orderservice.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

	Order findOrderByOrdernumber(String ordernumber);

}
