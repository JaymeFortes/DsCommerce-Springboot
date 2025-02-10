package com.DsCommerce.service;

import com.DsCommerce.dto.ProductDTO;
import com.DsCommerce.entities.Product;
import com.DsCommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        return new ProductDTO(product);
    }
}
