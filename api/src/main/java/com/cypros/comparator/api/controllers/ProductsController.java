package com.cypros.comparator.api.controllers;

import com.cypros.comparator.api.entities.Product;
import com.cypros.comparator.api.requests.LoginRequest;
import com.cypros.comparator.api.responses.AuthenticationResponse;
import com.cypros.comparator.api.services.ProductsServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsController {

  private final ProductsServices productsServices;

  public ProductsController(ProductsServices productsServices) {
    this.productsServices = productsServices;
  }

  @GetMapping(path = "/api/v1/products")
  public Page<Product> getProducts(Pageable pageable) {
    return productsServices.getAllProducts(pageable);
  }
/*  @PostMapping("/login")
  public RegistrationResponse login(@RequestBody LoginRequest loginRequest){
    return  userServices.loginService(loginRequest);
  }*/
}
