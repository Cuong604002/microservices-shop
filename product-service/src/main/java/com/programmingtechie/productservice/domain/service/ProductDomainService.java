package com.programmingtechie.productservice.domain.service;

import com.programmingtechie.productservice.domain.model.Product;
import com.programmingtechie.productservice.domain.repository.ProductRepository;
import com.programmingtechie.productservice.infrastructure.support.SequenceGeneratorService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDomainService {

    private final ProductRepository productRepository;
    private final SequenceGeneratorService sequenceGenerator;

    // 1. Logic Tạo sản phẩm (Sinh ID SP001 ở đây)
    public Product createProduct(String name, String desc, java.math.BigDecimal price, String skuCode) {
        long seq = sequenceGenerator.generateSequence(Product.SEQUENCE_NAME);
        String customId = String.format("SP%03d", seq);

        Product product = Product.builder()
                .id(customId)
                .name(name)
                .description(desc)
                .price(price)
                .skuCode(skuCode)
                .build();

        return productRepository.save(product);
    }

    // 2. Logic Lấy tất cả
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 3. Logic Cập nhật
    public Product updateProduct(String skuCode, String name, String desc, BigDecimal price) {
        Product product = productRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new RuntimeException("Product not found: " + skuCode));

        product.updateProductDetails(name, desc, price);

        // 2. Phải có lệnh return ở đây
        return productRepository.save(product);
    }
    // 4. Logic Xóa
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

}