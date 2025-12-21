package com.programmingtechie.productservice.infrastructure.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "database_sequences")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseSequence {
    @Id
    private String id; // Tên sequence (ví dụ "product_seq")
    private long seq;  // Giá trị hiện tại
}
