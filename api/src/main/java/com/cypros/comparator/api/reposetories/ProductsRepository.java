package com.cypros.comparator.api.reposetories;

import com.cypros.comparator.api.entities.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductsRepository extends
     MongoRepository<Product, String> {

}
