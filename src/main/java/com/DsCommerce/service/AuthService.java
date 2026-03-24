package com.DsCommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DsCommerce.entities.User;
import com.DsCommerce.exceptions.ForbiddenException;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    public void validateSelfOrAdmin(Long userId) {
        User me = userService.authenticate();

        if (me.hasRole("ROLE_ADMIN")) {
            return;
        }

        if (!me.getId().equals(userId)) {
            throw new ForbiddenException("Acess denied. Should be self or admin");
        }
    }
}
