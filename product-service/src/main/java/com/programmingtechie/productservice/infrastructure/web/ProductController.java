package com.programmingtechie.productservice.infrastructure.web; // <--- Package mới

import com.programmingtechie.productservice.domain.model.Product;
import com.programmingtechie.productservice.domain.service.ProductDomainService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController { // Tên gọi chuẩn DDD là Adapter

    // CHỈ GỌI SERVICE, KHÔNG GỌI REPOSITORY
    private final ProductDomainService productDomainService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest request) {
        productDomainService.createProduct(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getSkuCode()
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts() {
        return productDomainService.getAllProducts();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateProduct(@PathVariable String id, @RequestBody ProductRequest request) {
        productDomainService.updateProduct(
                id,
                request.getName(),
                request.getDescription(),
                request.getPrice()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String id) {
        productDomainService.deleteProduct(id);
    }

    @Data
    public static class ProductRequest {
        private String name;
        private String description;
        private BigDecimal price;
        private String skuCode;
    }
}