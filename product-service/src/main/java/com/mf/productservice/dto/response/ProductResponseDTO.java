package com.mf.productservice.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

	private String productname;
	private String idsku;
	private String brandname;
	private BigDecimal priceperunit;
	private String categoryname;
}
