package com.programmingtechie.inventoryservice.application;

import com.programmingtechie.inventoryservice.domain.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    /**
     * Hàm này được gọi bởi InventoryGrpcAdapter
     * Nhiệm vụ: Kiểm tra xem sản phẩm có mã skuCode có đủ số lượng không.
     */
    @Transactional(readOnly = true)
    public boolean checkStock(String skuCode, Integer quantityRequired) {
        log.info("Checking stock for SKU: {}", skuCode);

        // Logic:
        // 1. Tìm trong DB xem có sản phẩm này không
        // 2. Nếu có -> kiểm tra số lượng tồn (quantity) >= số lượng cần mua (quantityRequired)
        // 3. Nếu không tìm thấy hoặc không đủ hàng -> trả về false
        return inventoryRepository.findBySkuCode(skuCode)
                .map(inventory -> inventory.getQuantity() >= quantityRequired)
                .orElse(false);
    }
}