package co.edu.uis.catalogo.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import co.edu.uis.catalogo.model.Producto;

/**
 * Respuesta de {@code GET /productos}, con la forma exacta del contrato (sección 2).
 *
 * <p>{@code total} es la cantidad de productos que cumplen el filtro, <b>no</b> los de esta página:
 * es lo que el frontend necesita para dibujar cuántas páginas hay.
 */
public record ProductoListadoResponse(List<ProductoResponse> productos, long total,
		int pagina, int tamanoPagina) {

	/**
	 * Convierte una página de MongoDB en la respuesta de la API. {@code total} sale de
	 * {@code getTotalElements()}, no de {@code getContent().size()}: si hay 42 productos y pides 20,
	 * la respuesta trae 20 en productos y 42 en total.
	 */
	public static ProductoListadoResponse desde(Page<Producto> pagina, int numeroPagina,
			int tamanoPagina) {
		return new ProductoListadoResponse(
				pagina.getContent().stream().map(ProductoResponse::desde).toList(),
				pagina.getTotalElements(), numeroPagina, tamanoPagina);
	}

}