package co.edu.uis.catalogo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uis.catalogo.dto.DescuentoStockRequest;
import co.edu.uis.catalogo.dto.DescuentoStockResponse;
import co.edu.uis.catalogo.dto.ProductoRequest;
import co.edu.uis.catalogo.dto.ProductoResponse;
import co.edu.uis.catalogo.service.ProductoService;

import jakarta.validation.Valid;

/**
 * Endpoints de productos (contrato, sección 2). El servicio expone {@code /productos}; el prefijo
 * {@code /api/catalogo} lo agrega Kong.
 *
 * <p>Aquí no se manejan errores: el service lanza excepciones y
 * {@link co.edu.uis.catalogo.error.ManejadorGlobalErrores} las convierte en el formato del contrato.
 */
@RestController
@RequestMapping("/productos")
public class ProductoController {

	private final ProductoService productoService;

	public ProductoController(ProductoService productoService) {
		this.productoService = productoService;
	}

	/**
	 * {@code @Valid} es lo que dispara las validaciones del record. Sin esa anotación, las reglas
	 * {@code @NotBlank} y compañía quedan escritas pero nadie las ejecuta.
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProductoResponse crear(@Valid @RequestBody ProductoRequest datos) {
		return productoService.crear(datos);
	}

	@PutMapping("/{id}")
	public ProductoResponse actualizar(@PathVariable String id, @Valid @RequestBody ProductoRequest datos) {
		return productoService.actualizar(id, datos);
	}

	/** POST y no PUT: no reemplaza un recurso, dispara una acción sobre él (contrato v2.3). */
	@PostMapping("/{id}/activar")
	public ProductoResponse activar(@PathVariable String id) {
		return productoService.activar(id);
	}

	/**
	 * Lo llama Carro al confirmar el checkout, no el navegador. Va antes que {@code /{id}/activar} en
	 * importancia, pero no choca con él: Spring prefiere siempre la ruta literal sobre la que tiene
	 * variable, así que {@code descontar-stock} nunca se confunde con un id.
	 */
	@PostMapping("/descontar-stock")
	public DescuentoStockResponse descontarStock(@Valid @RequestBody DescuentoStockRequest peticion) {
		return productoService.descontarStock(peticion);
	}

}