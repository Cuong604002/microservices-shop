package com.programmingtechie.orderservice.infrastructure.web;

import com.programmingtechie.orderservice.domain.service.OrderDomainService;
import com.programmingtechie.orderservice.dto.OrderRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.programmingtechie.orderservice.domain.model.Order;
import java.util.List;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderDomainService orderDomainService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        return CompletableFuture.supplyAsync(() -> orderDomainService.placeOrder(orderRequest));
    }
    // --- THÊM MỚI: API SỬA ĐƠN HÀNG (PUT) ---
    @PutMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<String> updateOrder(@PathVariable Long orderId, @RequestBody OrderRequest orderRequest) {
        // Logic sửa cũng cần check kho nên vẫn nên dùng CompletableFuture
        return CompletableFuture.supplyAsync(() -> orderDomainService.updateOrder(orderId, orderRequest));
    }

    // --- THÊM MỚI: API XÓA ĐƠN HÀNG (DELETE) ---
    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteOrder(@PathVariable Long orderId) {
        orderDomainService.deleteOrder(orderId);
        return "Xóa đơn hàng thành công!";
    }
    // --- THÊM MỚI: API LẤY TẤT CẢ ĐƠN HÀNG (GET ALL) ---
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getAllOrders() {
        return orderDomainService.getAllOrders();
    }

    // --- THÊM MỚI: API LẤY CHI TIẾT 1 ĐƠN HÀNG (GET BY ID) ---
    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public Order getOrderById(@PathVariable Long orderId) {
        return orderDomainService.getOrderById(orderId);
    }

    // Hàm dự phòng: Nếu gọi Inventory lỗi hoặc quá lâu thì chạy hàm này
//    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
//        return CompletableFuture.supplyAsync(() -> "Rất tiếc! Có lỗi xảy ra khi gọi kho hàng, vui lòng đặt lại sau.");
//    }
}