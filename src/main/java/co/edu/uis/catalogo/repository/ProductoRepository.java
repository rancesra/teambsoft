package co.edu.uis.catalogo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.uis.catalogo.model.Producto;

/**
 * Acceso a la colección de productos. Al heredar de {@code MongoRepository} ya vienen hechos
 * {@code save}, {@code findById}, {@code findAll} y {@code deleteAll}.
 *
 * <p>Los cinco métodos de abajo no tienen cuerpo a propósito: Spring Data los implementa solo,
 * leyendo el nombre. {@code findByActivoTrueAndCategoria} se traduce a "buscar donde activo sea
 * verdadero y la categoría sea esta". Si se escribe mal un nombre de campo, la aplicación falla al
 * arrancar, no en tiempo de ejecución.
 *
 * <p>El orden importa para el rendimiento: {@link Producto} declara el índice compuesto
 * {@code {activo, categoria}}, y un índice compuesto se puede usar empezando por la izquierda. Por eso
 * los métodos que filtran por activo y luego por categoría sí lo aprovechan.
 */
public interface ProductoRepository extends MongoRepository<Producto, String> {

	/** Solo los productos que están a la venta (el listado por defecto). */
	Page<Producto> findByActivoTrue(Pageable paginado);

	/** Solo los productos que están a la venta de una categoría. */
	Page<Producto> findByActivoTrueAndCategoria(String categoria, Pageable paginado);

	/** Solo los desactivados, para que el administrador pueda encontrarlos y reactivarlos (Historia 8). */
	Page<Producto> findByActivoFalse(Pageable paginado);

	/** Desactivados de una categoría. */
	Page<Producto> findByActivoFalseAndCategoria(String categoria, Pageable paginado);

	/** Todos, de una categoría: es lo que se usa con {@code ?activo=todos&categoria=...}. */
	Page<Producto> findByCategoria(String categoria, Pageable paginado);

}