package com.cibertec.repositories;

import org.springframework.data.repository.CrudRepository;

import com.cibertec.models.Product;

public interface ProductDao extends CrudRepository<Product, Long>{

	
}
