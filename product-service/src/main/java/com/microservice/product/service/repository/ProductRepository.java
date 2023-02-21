package com.microservice.product.service.repository;

import com.microservice.product.service.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product,String> {
    Optional <Product> findByName(String name);
}
