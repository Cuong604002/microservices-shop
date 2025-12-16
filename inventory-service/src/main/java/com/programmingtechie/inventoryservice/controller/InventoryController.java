package com.programmingtechie.inventoryservice.controller;

import com.programmingtechie.inventoryservice.dto.InventoryRequest;
import com.programmingtechie.inventoryservice.dto.InventoryResponse;
import com.programmingtechie.inventoryservice.service.InventoryService;
import com.programmingtechie.inventoryservice.model.Inventory; // Import Model
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // 1. POST: Tạo mới/Nhập kho
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createInventory(@RequestBody InventoryRequest inventoryRequest) {
        inventoryService.createInventory(inventoryRequest);
    }

    // 2. GET: Xem tất cả kho
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Inventory> getAllInventory() {
        return inventoryService.getAllInventory();
    }

    // 3. PUT: Cập nhật số lượng theo ID (Long id)
    // URL: http://localhost:8181/api/inventory/{id}
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateInventory(@PathVariable String id, @RequestBody InventoryRequest inventoryRequest) {
        inventoryService.updateInventory(id, inventoryRequest);
    }

    // 4. DELETE: Xóa kho theo ID (Long id)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInventory(@PathVariable String id) {
        inventoryService.deleteInventory(id);
    }

    // API cũ để kiểm tra hàng (giữ nguyên để Order Service gọi)
    @GetMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }
}