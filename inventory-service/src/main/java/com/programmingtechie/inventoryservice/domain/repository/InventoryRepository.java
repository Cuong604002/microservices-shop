package com.programmingtechie.inventoryservice.domain.repository;

import com.programmingtechie.inventoryservice.domain.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> { // Lưu ý: Dùng Long, không dùng String

    // ĐÂY LÀ HÀM BẠN ĐANG THIẾU
    // Tìm kiếm chính xác 1 sản phẩm theo mã skuCode
    Optional<Inventory> findBySkuCode(String skuCode);
}