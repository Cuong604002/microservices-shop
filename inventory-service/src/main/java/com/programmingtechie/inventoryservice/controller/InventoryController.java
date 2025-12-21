package com.programmingtechie.inventoryservice.controller;

import com.programmingtechie.inventoryservice.domain.model.Inventory;
import com.programmingtechie.inventoryservice.domain.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryRepository inventoryRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addInventory(@RequestBody InventoryRequest inventoryRequest) {
        Inventory inventory = new Inventory();
        inventory.setSkuCode(inventoryRequest.getSkuCode());
        inventory.setQuantity(inventoryRequest.getQuantity());

        inventoryRepository.save(inventory);
        return "Đã thêm tồn kho thành công!";
    }

    // Class DTO nhỏ để hứng dữ liệu từ Postman
    @Data
    public static class InventoryRequest {
        private String skuCode;
        private Integer quantity;
    }
}