package com.programmingtechie.orderservice.domain.service;

// --- SỬA IMPORT DÒNG NÀY ---
import com.programmingtechie.orderservice.infrastructure.grpc.OrderGrpcClient;

import com.programmingtechie.orderservice.domain.event.OrderPlacedEvent;
import com.programmingtechie.orderservice.domain.model.Order;
import com.programmingtechie.orderservice.domain.model.OrderLineItems;
import com.programmingtechie.orderservice.domain.repository.OrderRepository;
import com.programmingtechie.orderservice.dto.OrderLineItemsDto;
import com.programmingtechie.orderservice.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderDomainService {

    private final OrderRepository orderRepository;

    // --- SỬA TÊN BIẾN Ở ĐÂY ---
    private final OrderGrpcClient orderGrpcClient; // Đổi từ InventoryClient thành OrderGrpcClient

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItems);

        // --- GỌI QUA CLASS MỚI ---
        boolean allProductsInStock = order.getOrderLineItemsList().stream()
                .allMatch(item -> orderGrpcClient.checkStock(item.getSkuCode(), item.getQuantity()));

        if (allProductsInStock) {
            orderRepository.save(order);

            // Gửi Kafka
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber(), "customer@gmail.com"));
            log.info("Notification sent for Order Number: {}", order.getOrderNumber());

            return "Đặt hàng thành công!";
        } else {
            throw new IllegalArgumentException("Sản phẩm không có trong kho hoặc số lượng không đủ!");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
    public String updateOrder(Long orderId, OrderRequest orderRequest) {
        // Tìm đơn hàng cũ
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId));

        // Convert DTO mới sang List Entity
        List<OrderLineItems> updatedItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        // QUAN TRỌNG: Sửa đơn hàng thì cũng phải check kho xem số lượng mới có đủ không
        boolean allProductsInStock = updatedItems.stream()
                .allMatch(item -> orderGrpcClient.checkStock(item.getSkuCode(), item.getQuantity()));

        if (allProductsInStock) {
            // Cập nhật lại danh sách hàng hóa
            existingOrder.getOrderLineItemsList().clear(); // Xóa list cũ
            existingOrder.getOrderLineItemsList().addAll(updatedItems); // Thêm list mới

            orderRepository.save(existingOrder);

            return "Cập nhật đơn hàng thành công!";
        } else {
            throw new IllegalArgumentException("Sản phẩm cập nhật không đủ hàng trong kho!");
        }
    }

    // --- 2. HÀM XÓA ĐƠN HÀNG ---
    public void deleteOrder(Long orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
            log.info("Đã xóa đơn hàng ID: {}", orderId);
        } else {
            throw new RuntimeException("Không tìm thấy đơn hàng để xóa!");
        }
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 2. Lấy 1 đơn hàng theo ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng có ID: " + id));
    }

}