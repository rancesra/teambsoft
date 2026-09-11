package co.edu.uis.catalogo.error;

public class ProductoNoEncontradoException extends RuntimeException {

	public ProductoNoEncontradoException(String id) {
		super("No existe un producto con id " + id);
	}

}