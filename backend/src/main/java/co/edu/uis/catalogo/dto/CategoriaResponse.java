package co.edu.uis.catalogo.dto;

import co.edu.uis.catalogo.model.Categoria;

public record CategoriaResponse(String id, String nombre) {

	public static CategoriaResponse desde(Categoria categoria) {
		return new CategoriaResponse(categoria.getId(), categoria.getNombre());
	}

}