package com.DsCommerce.services;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.DsCommerce.entities.User;
import com.DsCommerce.exceptions.ForbiddenException;
import com.DsCommerce.service.AuthService;
import com.DsCommerce.service.UserService;
import com.DsCommerce.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {

    @InjectMocks
    private AuthService service;

    @Mock
    private UserService userService;

    private User admin, selfClient, otherClient;

    @BeforeEach
    void setUp() throws Exception {
        admin = UserFactory.createAdminUser();
        selfClient = UserFactory.createCustomClientUser(1L, "Bob");
        otherClient = UserFactory.createCustomClientUser(2L, "Maria");
    }

    @Test
    public void validateSelfOrAdminShouldDoNothingWhenAdmin() {
        when(userService.authenticate()).thenReturn(admin);

        Long userId = admin.getId();

        Assertions.assertDoesNotThrow(() -> {
            service.validateSelfOrAdmin(userId); // Deve passar sem lançar exceção, pois o usuário é admin
        });
    }

    @Test
    public void validateSelfOrAdminShouldDoNothingWhenSelfLogged() {
        when(userService.authenticate()).thenReturn(selfClient);

        Long userId = selfClient.getId();

        Assertions.assertDoesNotThrow(() -> {
            service.validateSelfOrAdmin(userId); // Deve passar sem lançar exceção, pois o usuário é ele mesmo
        });
    }

    @Test
    public void validateSelfOrAdminShouldThrowForbiddenExceptionWhenNotAdminAndNotSelf() {
        when(userService.authenticate()).thenReturn(selfClient); // Simula o usuário autenticado como selfClient, que não é admin

        Long userId = otherClient.getId(); // Tenta acessar os dados de outro cliente (otherClient), que não é admin nem ele mesmo

        Assertions.assertThrows(ForbiddenException.class, () -> {
            service.validateSelfOrAdmin(userId); // Deve lançar ForbiddenException, pois o usuário não é admin nem ele
                                                 // mesmo
        });
    }
}
