package com.mf.orderservice.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {	
	
	private Long idcategory;	
	private String categoryname;
	private List<Product> products;
}