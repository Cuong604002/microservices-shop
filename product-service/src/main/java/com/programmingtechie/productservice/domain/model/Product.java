package com.programmingtechie.productservice.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@Document(value = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Transient // Biến này chỉ dùng để định danh tên bộ đếm, không lưu vào DB
    public static final String SEQUENCE_NAME = "product_sequence";
    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String skuCode;
    // --- DDD BEHAVIOR (Hành vi nghiệp vụ) ---
    public void updateProductDetails(String name, String description, BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Giá tiền không được âm!");
        }
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
