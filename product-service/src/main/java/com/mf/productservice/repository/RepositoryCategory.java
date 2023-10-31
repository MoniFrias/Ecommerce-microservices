package com.mf.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mf.productservice.entity.Category;

@Repository
public interface RepositoryCategory extends JpaRepository<Category, Long>{

	Category findCategoryByCategoryname(String categoryname);

	Category findCategoryByIdcategory(Long idcategory);

}
