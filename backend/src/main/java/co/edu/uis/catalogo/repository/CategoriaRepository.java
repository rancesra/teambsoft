package co.edu.uis.catalogo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.uis.catalogo.model.Categoria;

public interface CategoriaRepository extends MongoRepository<Categoria, String> {
}