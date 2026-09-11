package co.edu.uis.catalogo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.uis.catalogo.model.Producto;

public interface ProductoRepository extends MongoRepository<Producto, String> {
}