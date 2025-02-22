package com.DsCommerce.service;

import com.DsCommerce.dto.OrderDTO;
import com.DsCommerce.entities.Order;
import com.DsCommerce.exceptions.ResourceNotFoundException;
import com.DsCommerce.repository.OrderRepository;
import com.DsCommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado!"));
        return new OrderDTO(order);

    }



}
