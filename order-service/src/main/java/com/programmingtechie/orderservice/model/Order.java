package com.programmingtechie.orderservice.model;

import com.programmingtechie.orderservice.utils.StringPrefixedSequenceIdGenerator; // Nhớ import cái file vừa tạo
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.List;

@Entity
@Table(name = "t_orders") // Đặt tên bảng là t_orders cho an toàn
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @GenericGenerator(
            name = "order_seq",
            strategy = "com.programmingtechie.orderservice.utils.StringPrefixedSequenceIdGenerator", // Trỏ đúng đường dẫn file ở Bước 1
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "DH"), // <--- Sửa chữ DH ở đây
                    @Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%03d") // %03d nghĩa là 001, 002...
            }
    )
    private String id; // Lưu ý: Kiểu dữ liệu phải là String nhé

    private String orderNumber;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderLineItems> orderLineItemsList;
}