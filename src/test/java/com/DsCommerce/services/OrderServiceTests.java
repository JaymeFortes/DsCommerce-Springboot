package com.DsCommerce.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.DsCommerce.dto.OrderDTO;
import com.DsCommerce.entities.Order;
import com.DsCommerce.entities.User;
import com.DsCommerce.exceptions.ResourceNotFoundException;
import com.DsCommerce.repository.OrderRepository;
import com.DsCommerce.service.AuthService;
import com.DsCommerce.service.OrderService;
import com.DsCommerce.tests.OrderFactory;
import com.DsCommerce.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderService service;

    @Mock
    private OrderRepository repository;

    @Mock
    private AuthService authService;

    private Long existingId, nonExistingId;

    private Order order;

    private OrderDTO orderDTO;

    private User admin, client;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;

        admin = UserFactory.createCustomAdminUser(2L, "Admin");
        client = UserFactory.createCustomClientUser(1L, "Bob");

        order = OrderFactory.createOrder(client);
        orderDTO = new OrderDTO(order);

        when(repository.findById(existingId)).thenReturn(Optional.of(order));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
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
}
