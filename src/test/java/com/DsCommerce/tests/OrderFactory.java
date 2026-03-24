package com.DsCommerce.tests;

import java.time.Instant;

import com.DsCommerce.entities.Order;
import com.DsCommerce.entities.OrderItem;
import com.DsCommerce.entities.Payment;
import com.DsCommerce.entities.Product;
import com.DsCommerce.entities.User;
import com.DsCommerce.enums.OrderStatus;

public class OrderFactory {

    public static Order createOrder(User client) {

        Order order = new Order(1L, Instant.now(), OrderStatus.WAITING_PAYMENT, client, new Payment());

        Product product = ProductFactory.createProduct();
        OrderItem orderItem = new OrderItem(order, product, 2, product.getPrice());
        order.getItems().add(orderItem);

        return order;
    }
}
