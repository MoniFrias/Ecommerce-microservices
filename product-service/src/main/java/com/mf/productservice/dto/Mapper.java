package com.mf.productservice.dto;

import com.mf.productservice.dto.request.ProductRequestDTO;
import com.mf.productservice.dto.request.UpdateProductRequestDTO;
import com.mf.productservice.dto.response.CategoryResponseDTO;
import com.mf.productservice.dto.response.ProductResponseDTO;
import com.mf.productservice.entity.Category;
import com.mf.productservice.entity.Product;

public class Mapper {
	
	public static Product productRequestDtoToProductEntity(ProductRequestDTO productDto, Category category) {
		Product product =  new Product();
		product.setProductname(productDto.getProductname());
		product.setIdsku(productDto.getIdsku());
		product.setDescription(productDto.getDescription());
		product.setBrandname(productDto.getBrandname());
		product.setPriceperunit(productDto.getPriceperunit()); 
		product.setStock(productDto.getStock());
		product.setCategory(category);
		return product;
	}
	
	public static Product productRequestDtoToProductEntity(UpdateProductRequestDTO productDto, Category category) {
		Product product =  new Product();
		product.setProductname(productDto.getProductname());
		product.setDescription(productDto.getDescription());
		product.setBrandname(productDto.getBrandname());
		product.setPriceperunit(productDto.getPriceperunit()); 
		product.setStock(productDto.getStock());
		product.setCategory(category);
		return product;
	}

	public static ProductResponseDTO productToProductResponseDto(Product product, String categoryname) {
		ProductResponseDTO responseProduct = new ProductResponseDTO();
		responseProduct.setProductname(product.getProductname());
		responseProduct.setIdsku(product.getIdsku());
		responseProduct.setCategoryname(categoryname);
		responseProduct.setBrandname(product.getBrandname());
		responseProduct.setPriceperunit(product.getPriceperunit());
		return responseProduct;
	}

	public static CategoryResponseDTO categoryToCategoryResponseDto(Category category) {
		CategoryResponseDTO categoryResponse = new CategoryResponseDTO();
		categoryResponse.setCategoryname(category.getCategoryname());
		return categoryResponse;
	}

	public static Product updateValuesProduct(Product productFound, Product productUpdated) {
		productFound.setProductname(productUpdated.getProductname());
		productFound.setDescription(productUpdated.getDescription());
		productFound.setBrandname(productUpdated.getBrandname());
		productFound.setPriceperunit(productUpdated.getPriceperunit());
		productFound.setStock(productUpdated.getStock());
		productFound.setCategory(productUpdated.getCategory());
		return productFound;
	}
	

}
