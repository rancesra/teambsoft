package co.edu.uis.catalogo.service;

import org.springframework.stereotype.Service;

import co.edu.uis.catalogo.error.ProductoNoEncontradoException;
import co.edu.uis.catalogo.model.Producto;
import co.edu.uis.catalogo.repository.CategoriaRepository;
import co.edu.uis.catalogo.repository.ProductoRepository;

@Service
public class ProductoService {

	private final ProductoRepository productoRepository;
	private final CategoriaRepository categoriaRepository;

	public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
		this.productoRepository = productoRepository;
		this.categoriaRepository = categoriaRepository;
	}

	// B2 (crear, actualizar) y B3 (listar, obtener, desactivar) agregan sus métodos aquí.

	private Producto buscarExistente(String id) {
		return productoRepository.findById(id)
				.orElseThrow(() -> new ProductoNoEncontradoException(id));
	}

}
