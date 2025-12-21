package com.programmingtechie.orderservice.domain.repository;

import com.programmingtechie.orderservice.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
