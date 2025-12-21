package com.programmingtechie.orderservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Table(name = "t_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Tạm thời dùng Long và Auto Increment

    private String orderNumber;

    @OneToMany(cascade = CascadeType.ALL)

    private List<OrderLineItems> orderLineItemsList;
}