package com.mf.productservice.dto.request;

import java.math.BigDecimal;

import jakarta.annotation.Nonnull;
import lombok.Data;

@Data
public class ProductRequestDTO {

	@Nonnull
	private String productname;
	@Nonnull
	private String idsku;
	@Nonnull
	private String description;
	@Nonnull
	private String brandname;
	@Nonnull
	private BigDecimal priceperunit;
	@Nonnull
	private Long stock;
	@Nonnull
	private String categoryname;
}
