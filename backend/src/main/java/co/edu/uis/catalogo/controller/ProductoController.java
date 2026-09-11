package co.edu.uis.catalogo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uis.catalogo.service.ProductoService;

@RestController
@RequestMapping("/productos")
public class ProductoController {

	private final ProductoService productoService;

	public ProductoController(ProductoService productoService) {
		this.productoService = productoService;
	}

	// B2 (POST, PUT) y B3 (GET, GET por id, DELETE) agregan sus endpoints aquí.

}