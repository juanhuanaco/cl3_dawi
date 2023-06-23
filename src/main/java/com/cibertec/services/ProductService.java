package com.cibertec.services;

import java.io.InputStream;

import com.cibertec.models.Product;

public interface ProductService {
	public void addProducto(Product prod);
	public InputStream getProductReport(String name, String description) throws Exception;
}
