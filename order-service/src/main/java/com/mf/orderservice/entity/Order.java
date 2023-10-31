package com.mf.orderservice.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_cart")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idorder;
	private String ordernumber;
	private Long idcart;
	private BigDecimal totalorderprice;
	@Enumerated(EnumType.STRING)
	private StatusOrder orderstatus;
	@Enumerated(EnumType.STRING)
	private StatusOrder paymentstatus;
	private String createdat;
	private String updatedat;
	
	
}
