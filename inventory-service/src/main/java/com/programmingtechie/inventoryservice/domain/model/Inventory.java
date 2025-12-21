package com.programmingtechie.inventoryservice.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_inventory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Trong code Java vẫn dùng CamelCase cho dễ gọi
    // Nhưng lưu vào DB sẽ là cột "sku_code"
    @Column(name = "sku_code")
    private String skuCode;

    private Integer quantity;
}