package com.DsCommerce.services;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.DsCommerce.entities.User;
import com.DsCommerce.projections.UserDetailsProjection;
import com.DsCommerce.repository.UserRepository;
import com.DsCommerce.service.UserService;
import com.DsCommerce.tests.UserDetailsFactory;
import com.DsCommerce.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    private String existingUser, nonExistingUser;
    private User user;
    private List<UserDetailsProjection> userDetailsProjections;

    @BeforeEach
    void setUp() throws Exception {

        existingUser = "maria@gmail.com";
        nonExistingUser = "user@gmail.com";

        user = UserFactory.createCustomClientUser(1L, existingUser);
        userDetailsProjections = UserDetailsFactory.createCustomAdminUser(existingUser);

        when(repository.searchUserAndRolesByEmail(existingUser)).thenReturn(userDetailsProjections);
        when(repository.searchUserAndRolesByEmail(nonExistingUser)).thenReturn(new ArrayList<>());

    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsWhenExistingUsername() {
        UserDetails result = service.loadUserByUsername(existingUser);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingUser, result.getUsername());
    }

    @Test
    public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenNonExistingUsername() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(nonExistingUser);
        });
    }
}
