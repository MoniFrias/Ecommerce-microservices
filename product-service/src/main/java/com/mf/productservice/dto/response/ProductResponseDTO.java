package com.mf.productservice.dto.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductResponseDTO {

	private String productname;
	private String idsku;
	//private String description;
	private String brandname;
	private BigDecimal priceperunit;
	//private Long stock;
	private String categoryname;
}
