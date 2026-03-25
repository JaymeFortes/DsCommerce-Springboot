package com.DsCommerce.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.DsCommerce.dto.OrderDTO;
import com.DsCommerce.entities.Order;
import com.DsCommerce.entities.OrderItem;
import com.DsCommerce.entities.Product;
import com.DsCommerce.entities.User;
import com.DsCommerce.exceptions.ResourceNotFoundException;
import com.DsCommerce.repository.OrderItemRepository;
import com.DsCommerce.repository.OrderRepository;
import com.DsCommerce.repository.ProductRepository;
import com.DsCommerce.service.AuthService;
import com.DsCommerce.service.OrderService;
import com.DsCommerce.service.UserService;
import com.DsCommerce.tests.OrderFactory;
import com.DsCommerce.tests.ProductFactory;
import com.DsCommerce.tests.UserFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderService service;

    @Mock
    private OrderRepository repository;

    @Mock
    private AuthService authService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserService userService;

    private Long existingId, nonExistingId, existingProductId, nonExistingProductId;

    private Order order;

    private OrderDTO orderDTO;

    private User admin, client;

    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;

        existingProductId = 1L;
        nonExistingProductId = 1000L;

        admin = UserFactory.createCustomAdminUser(2L, "Admin");
        client = UserFactory.createCustomClientUser(1L, "Bob");

        order = OrderFactory.createOrder(client);
        orderDTO = new OrderDTO(order);

        product = ProductFactory.createProduct();

        when(repository.findById(existingId)).thenReturn(Optional.of(order));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
        when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

        when(repository.save(any())).thenReturn(order);

        when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(order.getItems()));

    }

    @Test
    public void findByIdShouldReturneOrderDTOWhenIdExistsAndAdminLogged() {

        doNothing().when(authService).validateSelfOrAdmin(any()); // Simula a validação de acesso, permitindo que o
                                                                  // admin acesse os dados

        OrderDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
    }

    @Test
    public void findByIdShouldReturneOrderDTOWhenIdExistsAndSelfClientLogged() {

        doNothing().when(authService).validateSelfOrAdmin(any()); // Simula a validação de acesso, permitindo que o
                                                                  // cliente acesse seus próprios dados

        OrderDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    @Test
    public void insertShouldReturnOrderDTOWhenAdminLogged() {
        when(userService.authenticate()).thenReturn(admin);

        OrderDTO result = service.insert(orderDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
    }

    @Test
    public void insertShouldReturnOrderDTOWhenClientLogged() {
        when(userService.authenticate()).thenReturn(client);

        OrderDTO result = service.insert(orderDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
    }

    @Test
    public void insertShouldThrowsUsernameNotFoundExceptionWhenUserNotLogged() {

        doThrow(UsernameNotFoundException.class).when(userService).authenticate();

        order.setClient(new User());
        orderDTO = new OrderDTO(order);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            @SuppressWarnings("unused")
            OrderDTO result = service.insert(orderDTO);
        });

    }

    @Test
    public void insertShouldThrowsEntityNotFoundExceptionWhenOrderProductIdDoesNotExist() {

        when(userService.authenticate()).thenReturn(client);

        product.setId(nonExistingId);
        OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
        order.getItems().add(orderItem);

        orderDTO = new OrderDTO(order);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {

            @SuppressWarnings("unused")
            OrderDTO result = service.insert(orderDTO);
        });

    }
}
