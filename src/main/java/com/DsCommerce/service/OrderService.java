package com.DsCommerce.service;

import com.DsCommerce.dto.OrderDTO;
import com.DsCommerce.dto.OrderItemDTO;
import com.DsCommerce.entities.Order;
import com.DsCommerce.entities.OrderItem;
import com.DsCommerce.entities.Product;
import com.DsCommerce.entities.User;
import com.DsCommerce.enums.OrderStatus;
import com.DsCommerce.exceptions.ResourceNotFoundException;
import com.DsCommerce.repository.OrderItemRepository;
import com.DsCommerce.repository.OrderRepository;
import com.DsCommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado!"));
        authService.validateSelfOrAdmin(order.getClient().getId());
        return new OrderDTO(order);
    }

    @Transactional
    public OrderDTO insert(OrderDTO orderDTO) {
        Order order = new Order();

        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);

        User user = userService.authenticate();
        order.setClient(user);

        for (OrderItemDTO itemDto : orderDTO.getItems()) {
            Product product = productRepository.getById(itemDto.getProductId());
            OrderItem item = new OrderItem(order, product, itemDto.getQuantity(), product.getPrice());
            order.getItems().add(item);
        }

        orderRepository.save(order);
        orderItemRepository.saveAll(order.getItems());

        return new OrderDTO(order);
    }


}
