package co.edu.uis.catalogo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import co.edu.uis.catalogo.dto.DescuentoStockRequest;
import co.edu.uis.catalogo.dto.DescuentoStockResponse;
import co.edu.uis.catalogo.dto.ProductoRequest;
import co.edu.uis.catalogo.dto.ProductoResponse;
import co.edu.uis.catalogo.error.ProductoNoEncontradoException;
import co.edu.uis.catalogo.error.StockInsuficienteException;
import co.edu.uis.catalogo.error.ValidacionFallidaException;
import co.edu.uis.catalogo.model.Producto;
import co.edu.uis.catalogo.repository.CategoriaRepository;
import co.edu.uis.catalogo.repository.ProductoRepository;
import co.edu.uis.catalogo.repository.ProductoStockRepository;

/**
 * Lógica de negocio de los productos: las reglas del contrato, el 404 cuando un producto no existe y
 * el soft delete. Los controllers nunca hablan con los repositorios; siempre pasan por aquí.
 */
@Service
public class ProductoService {

	private final ProductoRepository productoRepository;

	/** Para validar que la categoría de un producto exista (B2). */
	private final CategoriaRepository categoriaRepository;

	/** Para el descuento atómico de stock, que MongoRepository no sabe hacer. */
	private final ProductoStockRepository productoStockRepository;

	public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository,
			ProductoStockRepository productoStockRepository) {
		this.productoRepository = productoRepository;
		this.categoriaRepository = categoriaRepository;
		this.productoStockRepository = productoStockRepository;
	}

	/** POST: crea un producto. Nace activo y MongoDB le asigna el id al guardarlo (Historia 1). */
	public ProductoResponse crear(ProductoRequest datos) {
		validarCategoriaExiste(datos.categoria());

		Producto producto = new Producto(datos.nombre(), datos.descripcion(), datos.precio(),
				datos.categoria(), datos.stock(), datos.imagenes());

		return ProductoResponse.desde(productoRepository.save(producto));
	}

	/**
	 * PUT: reemplaza el producto completo (Historia 4). No cambia {@code id} ni {@code activo}: sobre un
	 * producto inactivo responde 200 y el producto sigue inactivo.
	 */
	public ProductoResponse actualizar(String id, ProductoRequest datos) {
		Producto producto = buscarExistente(id);
		validarCategoriaExiste(datos.categoria());

		producto.actualizar(datos.nombre(), datos.descripcion(), datos.precio(), datos.categoria(),
				datos.stock(), datos.imagenes());

		return ProductoResponse.desde(productoRepository.save(producto));
	}

	/**
	 * Reactiva un producto desactivado (Historia 7). Es idempotente: si ya estaba activo, devuelve el
	 * producto sin guardar nada, igual que hace DELETE con uno que ya estaba inactivo.
	 */
	public ProductoResponse activar(String id) {
		Producto producto = buscarExistente(id);

		if (producto.isActivo()) {
			return ProductoResponse.desde(producto);
		}

		producto.activar();
		return ProductoResponse.desde(productoRepository.save(producto));
	}

	/**
	 * Descuenta stock de varios productos al confirmar un checkout (contrato v2.3, sección 2).
	 *
	 * <p>La regla es <b>o pasa todo, o no pasa nada</b>. Como esta entrega corre un MongoDB de un solo
	 * nodo, no hay transacciones entre documentos: si un ítem no alcanza, hay que devolver a mano lo que
	 * ya se descontó. Por eso se va guardando en {@code descontados} lo que va saliendo bien.
	 */
	public DescuentoStockResponse descontarStock(DescuentoStockRequest peticion) {
		validarSinRepetidos(peticion.items());

		List<DescuentoStockRequest.Item> descontados = new ArrayList<>();
		List<DescuentoStockResponse.Item> resultado = new ArrayList<>();

		for (DescuentoStockRequest.Item item : peticion.items()) {
			Producto producto = buscarExistente(item.id());

			if (!producto.isActivo()) {
				devolver(descontados);
				throw new ValidacionFallidaException(
						"El producto " + item.id() + " está desactivado y no se puede vender");
			}

			Integer restante = productoStockRepository.descontar(item.id(), item.cantidad());

			if (restante == null) {
				devolver(descontados);
				throw new StockInsuficienteException("No hay stock suficiente del producto " + item.id()
						+ ": se pidieron " + item.cantidad() + " unidades");
			}

			descontados.add(item);
			resultado.add(new DescuentoStockResponse.Item(item.id(), restante));
		}

		return new DescuentoStockResponse(resultado);
	}

	/**
	 * La categoría debe ser el id de una categoría existente (contrato, sección 3). Es 400 y no 404
	 * porque lo que falla es un dato del producto que se está enviando, no la dirección de la petición.
	 */
	private void validarCategoriaExiste(String categoria) {
		if (!categoriaRepository.existsById(categoria)) {
			throw new ValidacionFallidaException("La categoría '" + categoria + "' no existe");
		}
	}

	/** Deshace los descuentos de una petición que terminó fallando. */
	private void devolver(List<DescuentoStockRequest.Item> descontados) {
		for (DescuentoStockRequest.Item item : descontados) {
			productoStockRepository.devolver(item.id(), item.cantidad());
		}
	}

	/**
	 * Dos líneas con el mismo producto harían dos descuentos separados y la reversión quedaría a medias.
	 * Es más simple rechazarlo: que Carro sume las cantidades antes de enviar.
	 */
	private void validarSinRepetidos(List<DescuentoStockRequest.Item> items) {
		Set<String> vistos = new HashSet<>();
		for (DescuentoStockRequest.Item item : items) {
			if (!vistos.add(item.id())) {
				throw new ValidacionFallidaException("El producto " + item.id() + " viene repetido en la lista");
			}
		}
	}

	/**
	 * Busca un producto por id, esté activo o no. Si no existe lanza
	 * {@link ProductoNoEncontradoException}, que el manejador global convierte en 404. La usan GET por
	 * id, PUT y DELETE, para que la regla "404 solo si el id no existe" esté escrita una sola vez.
	 */
	private Producto buscarExistente(String id) {
		// findById devuelve un Optional: una "caja" que viene vacía si el producto no existe.
		return productoRepository.findById(id)
				.orElseThrow(() -> new ProductoNoEncontradoException(id));
	}

}