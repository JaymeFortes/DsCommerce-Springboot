package com.DsCommerce.entities;

import com.DsCommerce.enums.OrderStatus;

import java.time.Instant;

public class Order {

    private Long id;
    private Instant moment;
    private OrderStatus status;
}
