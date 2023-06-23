package com.cibertec.controllers;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.cibertec.models.Product;
import com.cibertec.services.ProductServiceImpl;

import net.sf.jasperreports.engine.JRException;

@Controller
public class MainController {

	@Autowired
	private ProductServiceImpl productService;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	
	@GetMapping({"/","/product/register"})
	public String registerProduct(@ModelAttribute("product") Product product, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().contentEquals("ROLE_ADMIN"))) {
			return "product_registration";
		} else {
			return "access_denied";
		}
	}
	
	@PostMapping({"/product/register"})
	public ResponseEntity<ByteArrayResource> registerProduct(@Validated @ModelAttribute("product") Product product, boolean shouldGeneratePdf, BindingResult bindingResult) throws IOException, JRException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().contentEquals("ROLE_ADMIN"))) {
			if (bindingResult.hasErrors() == false) {
				productService.addProducto(product);
				
				if(shouldGeneratePdf)
					return generateProductPdf(product.getName(), product.getDescription());
			}
			return getHtmlResourceAsResponseEntity("/templates/product_registration.html");
		} else {
			return getHtmlResourceAsResponseEntity("/templates/access_denied.html");
		}
	}
	
	private ResponseEntity<ByteArrayResource> generateProductPdf(String name, String description) throws IOException, JRException{
		InputStream pdfStream = this.productService.getProductReport(name, description);
		byte[] data = pdfStream.readAllBytes();
	    pdfStream.close();
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=constancia_producto.pdf");
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentLength(data.length);
       
        // Return the HTML content as a response
        return ResponseEntity.ok().headers(headers).body(new ByteArrayResource(data));
	}
	
	private ResponseEntity<ByteArrayResource> getHtmlResourceAsResponseEntity(String htmlPath) throws IOException{
		InputStream htmlStream = getClass().getResourceAsStream(htmlPath);
		byte[] data = htmlStream.readAllBytes();
        htmlStream.close();
		HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=example.html");
        headers.setContentType(MediaType.TEXT_HTML);
		headers.setContentLength(data.length);
        
        // Return the HTML content as a response
        return ResponseEntity.ok().headers(headers).body(new ByteArrayResource(data));
	}
	
}
