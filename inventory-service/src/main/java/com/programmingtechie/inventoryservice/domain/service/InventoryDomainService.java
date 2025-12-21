package com.programmingtechie.inventoryservice.domain.service;

import com.programmingtechie.inventoryservice.domain.model.Inventory;
import com.programmingtechie.inventoryservice.domain.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryDomainService {

    private final InventoryRepository inventoryRepository;

    /**
     * 1. LẤY THÔNG TIN KHO (READ)
     * Dùng transaction readOnly để tối ưu hiệu năng
     */
    @Transactional(readOnly = true)
    public Optional<Inventory> getInventory(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode);
    }

    /**
     * 2. NHẬP KHO (ADD STOCK)
     * Logic: Tìm xem có chưa.
     * - Nếu chưa: Tạo mới với số lượng 0.
     * - Nếu có rồi: Lấy số lượng cũ + số lượng nhập thêm.
     */
    @Transactional
    public Inventory addStock(String skuCode, Integer quantity) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElse(Inventory.builder()
                        .skuCode(skuCode)
                        .quantity(0)
                        .build());

        // Cộng dồn số lượng
        inventory.setQuantity(inventory.getQuantity() + quantity);

        return inventoryRepository.save(inventory);
    }

    /**
     * 3. ĐIỀU CHỈNH/KIỂM KÊ KHO (UPDATE STOCK)
     * Logic: Tìm hàng, nếu không thấy thì báo lỗi.
     * Nếu thấy: Ghi đè số lượng mới (không cộng dồn).
     */
    @Transactional
    public Inventory updateStock(String skuCode, Integer quantity) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm có mã: " + skuCode));

        // Ghi đè số lượng
        inventory.setQuantity(quantity);

        return inventoryRepository.save(inventory);
    }

    /**
     * 4. XÓA MÃ HÀNG (DELETE STOCK)
     * Logic: Tìm hàng, nếu có thì xóa khỏi Database.
     */
    @Transactional
    public void deleteStock(String skuCode) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm để xóa: " + skuCode));

        inventoryRepository.delete(inventory);
    }
    @Transactional(readOnly = true)
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }
}