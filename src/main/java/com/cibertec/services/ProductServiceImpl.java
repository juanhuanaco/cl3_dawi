package com.cibertec.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.models.Product;
import com.cibertec.repositories.ProductDao;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductDao productoDao;
	
	@Override
	public void addProducto(Product prod) {
		productoDao.save(prod);
		
	}

	@Override
	public InputStream getProductReport(String name, String description) throws JRException {
		try {
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("PRODUCT_NAME", name);
			parameters.put("PRODUCT_DESCRIPTION", description);
			JasperPrint jPrint = JasperFillManager.fillReport(getClass().getResourceAsStream("/jasper/cl3_product.jasper"), parameters, new JREmptyDataSource());
			return new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jPrint));
			
		} catch (JRException e) {
			throw e;
		}
	}

}
