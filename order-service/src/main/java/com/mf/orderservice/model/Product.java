package com.mf.orderservice.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	private Long idproduct;
	private String productname;
	private String idsku;
	private String description;
	private String brandname;
	private BigDecimal priceperunit;
	private Long stock;
	private Category category;
}

