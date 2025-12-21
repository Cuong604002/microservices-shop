package com.programmingtechie.inventoryservice.infrastructure.grpc;

import com.programmingtechie.inventory.*;
import com.programmingtechie.inventoryservice.domain.model.Inventory;
import com.programmingtechie.inventoryservice.domain.service.InventoryDomainService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class InventoryGrpcAdapter extends InventoryServiceGrpc.InventoryServiceImplBase {

    // Inject Domain Service (Không gọi trực tiếp Repository)
    private final InventoryDomainService inventoryDomainService;

    // ========================================================================
    // 1. KIỂM TRA TỒN KHO (READ)
    // ========================================================================
    @Override
    public void isInStock(IsInStockRequest request, StreamObserver<IsInStockResponse> responseObserver) {
        // Mặc dù trong file .proto bạn đặt là "sku_code" (snake_case)
        // Nhưng Java sẽ tự động chuyển thành "getSkuCodeList()" (camelCase)
        List<String> skuCodes = request.getSkuCodeList();

        List<InventoryResponse> inventoryResponses = skuCodes.stream().map(skuCode -> {
            // Gọi Domain Service để lấy dữ liệu
            Optional<Inventory> inventory = inventoryDomainService.getInventory(skuCode);

            // Logic mapping dữ liệu
            boolean isInStock = inventory.isPresent() && inventory.get().getQuantity() > 0;
            int quantity = inventory.map(Inventory::getQuantity).orElse(0);

            // Build response
            return InventoryResponse.newBuilder()
                    .setSkuCode(skuCode)      // Proto: sku_code -> Java: setSkuCode
                    .setIsInStock(isInStock)  // Proto: is_in_stock -> Java: setIsInStock
                    .setQuantity(quantity)
                    .build();
        }).collect(Collectors.toList());

        IsInStockResponse response = IsInStockResponse.newBuilder()
                .addAllItems(inventoryResponses)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // ========================================================================
    // 2. NHẬP KHO (ADD STOCK) - Cộng dồn số lượng
    // ========================================================================
    @Override
    public void addStock(AddStockRequest request, StreamObserver<InventoryResponse> responseObserver) {
        // Gọi Domain Service để xử lý nghiệp vụ cộng dồn
        Inventory updatedInventory = inventoryDomainService.addStock(
                request.getSkuCode(),
                request.getQuantity()
        );

        responseObserver.onNext(mapToResponse(updatedInventory));
        responseObserver.onCompleted();
    }

    // ========================================================================
    // 3. ĐIỀU CHỈNH KHO (UPDATE STOCK) - Ghi đè số lượng
    // ========================================================================
    @Override
    public void updateStock(UpdateStockRequest request, StreamObserver<InventoryResponse> responseObserver) {
        // Gọi Domain Service để xử lý nghiệp vụ ghi đè
        Inventory updatedInventory = inventoryDomainService.updateStock(
                request.getSkuCode(),
                request.getQuantity()
        );

        responseObserver.onNext(mapToResponse(updatedInventory));
        responseObserver.onCompleted();
    }

    // ========================================================================
    // 4. XÓA MÃ HÀNG (DELETE STOCK)
    // ========================================================================
    @Override
    public void deleteStock(DeleteStockRequest request, StreamObserver<DeleteStockResponse> responseObserver) {
        // Gọi Domain Service để xóa
        inventoryDomainService.deleteStock(request.getSkuCode());

        DeleteStockResponse response = DeleteStockResponse.newBuilder()
                .setMessage("Đã xóa thành công mã hàng: " + request.getSkuCode())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // --- HÀM PHỤ TRỢ (HELPER) ĐỂ MAP DỮ LIỆU ---
    private InventoryResponse mapToResponse(Inventory inventory) {
        return InventoryResponse.newBuilder()
                .setSkuCode(inventory.getSkuCode())
                .setIsInStock(inventory.getQuantity() > 0)
                .setQuantity(inventory.getQuantity())
                .build();
    }

    // 5. XEM TOÀN BỘ KHO (GET ALL)
    // ========================================================================
    @Override
    public void getAllStock(GetAllStockRequest request, StreamObserver<GetAllStockResponse> responseObserver) {
        // 1. Gọi Domain Service lấy List<Inventory>
        List<Inventory> inventories = inventoryDomainService.getAllInventory();

        // 2. Map sang List<InventoryResponse>
        List<InventoryResponse> inventoryResponses = inventories.stream()
                .map(this::mapToResponse) // Tái sử dụng hàm mapToResponse ở cuối file
                .collect(Collectors.toList());

        // 3. Đóng gói và trả về
        GetAllStockResponse response = GetAllStockResponse.newBuilder()
                .addAllItems(inventoryResponses)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}