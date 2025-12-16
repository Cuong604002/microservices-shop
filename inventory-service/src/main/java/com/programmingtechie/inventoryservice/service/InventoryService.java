package com.programmingtechie.inventoryservice.service;

import com.programmingtechie.inventoryservice.dto.InventoryRequest;
import com.programmingtechie.inventoryservice.dto.InventoryResponse;
import com.programmingtechie.inventoryservice.model.Inventory;
import com.programmingtechie.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    // 1. Tạo mới (Đã sửa để sinh mã KH001)
    public void createInventory(InventoryRequest inventoryRequest) {
        // B1: Lấy số thứ tự tiếp theo
        long seqNumber = sequenceGeneratorService.generateSequence("inventory_seq");

        // B2: Tạo mã ID dạng KH001
        String customId = String.format("KH%03d", seqNumber);

        // B3: Gán vào Inventory
        Inventory inventory = new Inventory();
        inventory.setId(customId); // <-- Quan trọng: Gán ID mới tạo vào đây
        inventory.setSkuCode(inventoryRequest.getSkuCode());
        inventory.setQuantity(inventoryRequest.getQuantity());

        inventoryRepository.save(inventory);
        log.info("Inventory {} created with ID: {}", inventory.getSkuCode(), customId);
    }

    // 2. Lấy tất cả
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    // 3. Cập nhật theo ID (Đã đổi Long -> String)
    public void updateInventory(String id, InventoryRequest inventoryRequest) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));

        // Cập nhật số lượng mới
        inventory.setQuantity(inventoryRequest.getQuantity());
        // inventory.setSkuCode(inventoryRequest.getSkuCode()); // Nếu muốn sửa cả skuCode

        inventoryRepository.save(inventory);
        log.info("Inventory {} updated", id);
    }

    // 4. Xóa theo ID (Đã đổi Long -> String)
    public void deleteInventory(String id) {
        if (inventoryRepository.existsById(id)) {
            inventoryRepository.deleteById(id);
            log.info("Inventory {} deleted", id);
        } else {
            throw new RuntimeException("Inventory not found with id: " + id);
        }
    }

    // Hàm kiểm tra hàng (giữ nguyên logic)
    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).toList();
    }
}