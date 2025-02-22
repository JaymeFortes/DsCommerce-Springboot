package com.DsCommerce.repository;

import com.DsCommerce.entities.OrderItem;
import com.DsCommerce.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {
}
