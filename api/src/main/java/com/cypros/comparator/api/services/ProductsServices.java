package com.cypros.comparator.api.services;

import com.cypros.comparator.api.entities.Product;
import com.cypros.comparator.api.reposetories.ProductsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductsServices {

  private final ProductsRepository productsRepository;

  public ProductsServices(
      ProductsRepository productsRepository) {
    this.productsRepository = productsRepository;
  }

  public Page<Product> getAllProducts(Pageable pageable) {
    return productsRepository.findAll(pageable);
  }

}
