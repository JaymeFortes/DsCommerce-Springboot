package com.DsCommerce.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.DsCommerce.dto.ProductDTO;
import com.DsCommerce.dto.ProductMinDTO;
import com.DsCommerce.entities.Product;
import com.DsCommerce.exceptions.ResourceNotFoundException;
import com.DsCommerce.exceptions.DatabaseException;

import com.DsCommerce.repository.ProductRepository;
import com.DsCommerce.service.ProductService;
import com.DsCommerce.tests.ProductFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private Product product;
    private ProductDTO productDto;

    private Long existingId, nonExistingId, dependentProductId;
    private String ProductName;
    private PageImpl<Product> page;

    @BeforeEach
    void setUp() throws Exception {

        ProductName = "PlayStation 5";
        product = ProductFactory.createProduct(ProductName);
        productDto = new ProductDTO(product);
        page = new PageImpl<>(List.of(product)); // Cria uma página contendo o produto criado

        existingId = 1L;
        nonExistingId = 999L;
        dependentProductId = 3L;

        when(repository.findById(existingId)).thenReturn(Optional.of(product)); // Simula o comportamento do método
                                                                                // findById() do repositório, retornando
                                                                                // um Optional contendo o produto criado
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        when(repository.searchByName(any(), (Pageable) any())).thenReturn(page);

        when(repository.save(any())).thenReturn(product);

        when(repository.getReferenceById(existingId)).thenReturn(product);
        when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(repository.existsById(existingId)).thenReturn(true);
        when(repository.existsById(dependentProductId)).thenReturn(true);
        when(repository.existsById(nonExistingId)).thenReturn(false);
        doNothing().when(repository).deleteById(existingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentProductId);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), product.getId());
        Assertions.assertEquals(result.getName(), product.getName());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);

        });
    }

    @Test
    public void findAllShouldReturnPagedProductMinDto() {

        Pageable pageable = PageRequest.of(0, 12);
        String name = "PlayStation 5";

        Page<ProductMinDTO> result = service.findAll(name, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void insertShouldReturnProductDTO() {

        ProductDTO result = service.insert(productDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), product.getId());
        Assertions.assertEquals(result.getName(), product.getName());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = service.update(existingId, productDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), product.getId());
        Assertions.assertEquals(result.getName(), product.getName());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, productDto);

        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);

        });
        verify(repository, times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);

        });
        verify(repository, times(0)).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentProductId);

        });
        verify(repository, times(1)).deleteById(dependentProductId);
    }
}
