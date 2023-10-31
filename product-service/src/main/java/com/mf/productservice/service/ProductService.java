package com.mf.productservice.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mf.productservice.dto.Mapper;
import com.mf.productservice.dto.request.ProductRequestDTO;
import com.mf.productservice.dto.request.UpdateProductRequestDTO;
import com.mf.productservice.dto.response.CategoryResponseDTO;
import com.mf.productservice.dto.response.ProductResponseDTO;
import com.mf.productservice.dto.response.Response;
import com.mf.productservice.entity.Category;
import com.mf.productservice.entity.Product;
import com.mf.productservice.exceptions.ValidationException;
import com.mf.productservice.repository.RepositoryCategory;
import com.mf.productservice.repository.RepositoryProduct;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final RepositoryProduct repositoryProduct;
	private final RepositoryCategory repositoryCategory;

	private static Logger logger = LogManager.getLogger(ProductService.class);

	public Response addProduct(ProductRequestDTO product) throws Exception {
		Product productFound = repositoryProduct.findProductByProductname(product.getProductname());

		if (Objects.isNull(productFound)) {
			Category categoryFound = repositoryCategory.findCategoryByCategoryname(product.getCategoryname());
			if (Objects.nonNull(categoryFound)) {
				Product newProduct = Mapper.productRequestDtoToProductEntity(product, categoryFound);
				repositoryProduct.save(newProduct);
				logger.info("Product saved!");
				return new Response(true, "Product was created successfully");
			}
			throw new ValidationException("The category does not exist");
		}
		throw new ValidationException("The product is already in the database");

	}

	public Response addCategory(String categoryname) throws Exception {
		Category categoryFound = repositoryCategory.findCategoryByCategoryname(categoryname);
		if (Objects.isNull(categoryFound)) {
			Category newCategory = new Category();
			newCategory.setCategoryname(categoryname);
			repositoryCategory.save(newCategory);
			logger.info("Category saved!");
			return new Response(true, "Category was created successfully");
		}
		throw new ValidationException("The category is already in the database");
	}

	public List<ProductResponseDTO> getAllProducts() {
		List<ProductResponseDTO> listProducts = repositoryProduct.findAll().stream().map(product -> {
			Category category = repositoryCategory.findCategoryByIdcategory(product.getCategory().getIdcategory());
			ProductResponseDTO responseProduct = Mapper.productToProductResponseDto(product,
					category.getCategoryname());
			return responseProduct;
		}).collect(Collectors.toList());

		if (!listProducts.isEmpty()) {
			return listProducts;
		}
		throw new ValidationException("There are no saved products");
	}

	public List<CategoryResponseDTO> getAllCategories() {
		List<CategoryResponseDTO> categories = repositoryCategory.findAll().stream().map(category -> {
			CategoryResponseDTO categoryResponse = Mapper.categoryToCategoryResponseDto(category);
			return categoryResponse;
		}).collect(Collectors.toList());

		if (!categories.isEmpty()) {
			return categories;
		}
		throw new ValidationException("There are no saved categories");
	}

	public Product getProductById(Long idproduct) {
		return repositoryProduct.findById(idproduct)
				.orElseThrow(() -> new ValidationException("There is no product with that id"));
	}

	public ProductResponseDTO getProductBySku(String sku) {
		Product productFound = repositoryProduct.findProductByIdsku(sku);
		if (Objects.nonNull(productFound)) {
			Category category = repositoryCategory.findCategoryByIdcategory(productFound.getCategory().getIdcategory());
			return Mapper.productToProductResponseDto(productFound, category.getCategoryname());
		}
		throw new ValidationException("There is no product with that sku");
	}

	public ProductResponseDTO getProductByName(String productName) {
		Product productFound = repositoryProduct.findProductByProductname(productName);
		if (Objects.nonNull(productFound)) {
			Category category = repositoryCategory.findCategoryByIdcategory(productFound.getCategory().getIdcategory());
			return Mapper.productToProductResponseDto(productFound, category.getCategoryname());
		}
		throw new ValidationException("There is no product with that name");

	}

	public List<ProductResponseDTO> getProductByBrand(String brandName) {
		List<ProductResponseDTO> productResponse = repositoryProduct.findProductsByBrandname(brandName).stream()
				.map(product -> {
					Category category = repositoryCategory
							.findCategoryByIdcategory(product.getCategory().getIdcategory());
					ProductResponseDTO responseProduct = Mapper.productToProductResponseDto(product,
							category.getCategoryname());
					return responseProduct;

				}).collect(Collectors.toList());

		if (!productResponse.isEmpty()) {
			return productResponse;
		}
		throw new ValidationException("There is no product of that brand");
	}

	public String updateStockAdmin(Long quantity, String sku) {
		Product productFound = repositoryProduct.findProductByIdsku(sku);
		if (Objects.nonNull(productFound)) {
			productFound.setStock(quantity);
			repositoryProduct.save(productFound);

			return "The stock was updated to: " + quantity;
		}
		throw new ValidationException("There is no product with that sku");
	}

	public String updateStockOrder(Long quantity, String sku, String status) {
		Product productFound = repositoryProduct.findProductByIdsku(sku);
		if (Objects.nonNull(productFound)) {
			Long stock = productFound.getStock();
			if (status.equals("createOrder")) {
				Long stocknew = stock - quantity;
				productFound.setStock(stocknew);
				repositoryProduct.save(productFound);

				return "The stock was updated from: " + stock + " to: " + stocknew;
			} else if (status.equals("cancelOrder")) {
				Long stocknew = stock + quantity;
				productFound.setStock(stocknew);
				repositoryProduct.save(productFound);

				return "The stock was updated from: " + stock + " to: " + stocknew;
			}
			throw new ValidationException("Invalid option!");
		}
		throw new ValidationException("There is no product with that sku");
	}

	public Response updateProduct(UpdateProductRequestDTO product, String sku) {
		Product productFound = repositoryProduct.findProductByIdsku(sku);
		if (Objects.nonNull(productFound)) {
			Category categoryFound = repositoryCategory
					.findCategoryByIdcategory(productFound.getCategory().getIdcategory());
			if (Objects.nonNull(categoryFound)) {
				Product mapperProduct = Mapper.productRequestDtoToProductEntity(product, categoryFound);
				Product productUpdated = Mapper.updateValuesProduct(productFound, mapperProduct);
				repositoryProduct.save(productUpdated);
				return new Response(true, "Product was updated successfully");
			}
			throw new ValidationException("The category does not exist");
		}
		throw new ValidationException("There is no product with that sku");
	}

	public Response updateCategory(String categoryname, Long categoryId) {
		Category categoryFound = repositoryCategory.findCategoryByIdcategory(categoryId);
		if (Objects.nonNull(categoryFound)) {
			categoryFound.setCategoryname(categoryname);
			repositoryCategory.save(categoryFound);
			return new Response(true, "Category was updated successfully");
		}
		throw new ValidationException("There is no category with that ID");
	}

	public Response deleteProduct(String sku) {
		Product productFound = repositoryProduct.findProductByIdsku(sku);
		if (Objects.nonNull(productFound)) {
			repositoryProduct.deleteById(productFound.getIdproduct());
			logger.info("Product removed!");
			return new Response(true, "Successfully removed");
		}
		throw new ValidationException("There is no product with that sku");
	}

}
