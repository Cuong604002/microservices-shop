package com.programmingtechie.orderservice.infrastructure.grpc;

import com.programmingtechie.inventory.InventoryServiceGrpc;
import com.programmingtechie.inventory.IsInStockRequest;
import com.programmingtechie.inventory.IsInStockResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class OrderGrpcClient { // Tên class khớp với file của bạn

    // "inventory-service" phải khớp với cấu hình trong application.properties
    @GrpcClient("inventory-service")
    private InventoryServiceGrpc.InventoryServiceBlockingStub inventoryStub;

    public boolean checkStock(String skuCode, Integer quantity) {
        try {
            // Tạo Request gRPC
            IsInStockRequest request = IsInStockRequest.newBuilder()
                    .addSkuCode(skuCode)
                    .build();

            // Gọi sang Inventory Service
            IsInStockResponse response = inventoryStub.isInStock(request);

            // Xử lý kết quả trả về
            boolean isStockAvailable = response.getItemsList().stream()
                    .anyMatch(item -> item.getSkuCode().equals(skuCode)
                            && item.getIsInStock()
                            && item.getQuantity() >= quantity);

            return isStockAvailable;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gọi Inventory Service: " + e.getMessage());
        }
    }
}