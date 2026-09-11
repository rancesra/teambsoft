package co.edu.uis.catalogo.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "productos")
@CompoundIndex(name = "activo_categoria", def = "{'activo': 1, 'categoria': 1}")
public class Producto {

	@Id
	private String id;
	private String nombre;
	private String descripcion;
	private BigDecimal precio;
	private String categoria;
	private int stock;
	private List<String> imagenes = new ArrayList<>();
	private boolean activo = true;

	public Producto() {
	}

	public Producto(String nombre, String descripcion, BigDecimal precio, String categoria, int stock,
			List<String> imagenes) {
		asignarDatos(nombre, descripcion, precio, categoria, stock, imagenes);
	}

	public void actualizar(String nombre, String descripcion, BigDecimal precio, String categoria, int stock,
			List<String> imagenes) {
		asignarDatos(nombre, descripcion, precio, categoria, stock, imagenes);
	}

	public void desactivar() {
		this.activo = false;
	}

	private void asignarDatos(String nombre, String descripcion, BigDecimal precio, String categoria, int stock,
			List<String> imagenes) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.categoria = categoria;
		this.stock = stock;
		this.imagenes = (imagenes != null) ? imagenes : new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public String getCategoria() {
		return categoria;
	}

	public int getStock() {
		return stock;
	}

	public List<String> getImagenes() {
		return imagenes;
	}

	public boolean isActivo() {
		return activo;
	}

}