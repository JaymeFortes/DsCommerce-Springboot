package com.DsCommerce.repository;

import com.DsCommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  ProductRepository extends JpaRepository<Product, Long> {


}
