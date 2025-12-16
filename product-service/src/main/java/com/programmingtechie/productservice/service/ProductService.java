package com.programmingtechie.productservice.service;

import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.model.Product;
import com.programmingtechie.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    // 👇 Inject Service tạo số thứ tự (Nhớ tạo file này nếu chưa có)
    private final SequenceGeneratorService sequenceGeneratorService;

    // 1. Tạo sản phẩm (Create) - Tự động sinh ID 1, 2, 3...
    public void createProduct(ProductRequest productRequest) {
        long seqNumber = sequenceGeneratorService.generateSequence(Product.SEQUENCE_NAME);
        String customId = String.format("SP%03d", seqNumber);
        Product product = Product.builder()
                // 👇 Gọi hàm sinh ID
                .id(customId)
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    // 2. Lấy danh sách (Read)
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    // 3. Cập nhật (Update) - ⚠️ Đã đổi tham số id thành Long
    public void updateProduct(String id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find product by id: " + id));

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());

        productRepository.save(product);
        log.info("Product {} is updated", product.getId());
    }

    // 4. Xóa (Delete) - ⚠️ Đã đổi tham số id thành Long
    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Cannot find product by id: " + id);
        }
        productRepository.deleteById(id);
        log.info("Product {} is deleted", id);
    }

    // Hàm phụ trợ map dữ liệu
    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId()) // ID này giờ là Long
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}