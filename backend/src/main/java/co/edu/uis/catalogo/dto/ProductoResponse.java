package co.edu.uis.catalogo.dto;

import java.math.BigDecimal;
import java.util.List;

import co.edu.uis.catalogo.model.Producto;

public record ProductoResponse(String id, String nombre, String descripcion, BigDecimal precio, String categoria,
		int stock, List<String> imagenes, boolean activo) {

	public static ProductoResponse desde(Producto producto) {
		return new ProductoResponse(producto.getId(), producto.getNombre(), producto.getDescripcion(),
				producto.getPrecio(), producto.getCategoria(), producto.getStock(), producto.getImagenes(),
				producto.isActivo());
	}

}