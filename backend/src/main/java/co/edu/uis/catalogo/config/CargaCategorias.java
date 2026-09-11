package co.edu.uis.catalogo.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import co.edu.uis.catalogo.model.Categoria;
import co.edu.uis.catalogo.repository.CategoriaRepository;

@Component
public class CargaCategorias implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(CargaCategorias.class);

	private final CategoriaRepository categoriaRepository;

	public CargaCategorias(CategoriaRepository categoriaRepository) {
		this.categoriaRepository = categoriaRepository;
	}

	@Override
	public void run(ApplicationArguments args) {
		List<Categoria> categorias = List.of(
				new Categoria("cat-ropa", "ropa"),
				new Categoria("cat-hogar", "hogar"),
				new Categoria("cat-electronica", "electrónica"));
		categoriaRepository.saveAll(categorias);
		log.info("Categorías precargadas: {}", categorias.size());
	}

}
