package com.programmingtechie.productservice.domain.repository;

import com.programmingtechie.productservice.domain.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findBySkuCode(String skuCode);
}
