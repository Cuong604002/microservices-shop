package com.programmingtechie.productservice.infrastructure.grpc;

import com.programmingtechie.product.*;
import com.programmingtechie.productservice.domain.model.Product;
import com.programmingtechie.productservice.domain.repository.ProductRepository;
import com.programmingtechie.productservice.domain.service.ProductDomainService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class ProductGrpcService extends ProductServiceGrpc.ProductServiceImplBase {

    private final ProductDomainService productDomainService;
    private final ProductRepository productRepository;

    // 1. LẤY CHI TIẾT 1 SẢN PHẨM (THEO SKU_CODE)
    @Override
    public void getProduct(GetProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        var product = productRepository.findBySkuCode(request.getSkuCode())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với SKU: " + request.getSkuCode()));
        responseObserver.onNext(mapToProductResponse(product));
        responseObserver.onCompleted();
    }

    // 2. LẤY TẤT CẢ DANH SÁCH SẢN PHẨM
    @Override
    public void getAllProducts(GetAllProductsRequest request, StreamObserver<AllProductsResponse> responseObserver) {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> grpcProducts = products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
        AllProductsResponse response = AllProductsResponse.newBuilder()
                .addAllProducts(grpcProducts)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // 3. THÊM MỚI SẢN PHẨM (GỌI DOMAIN SERVICE ĐỂ SINH ID SP00X)
    @Override
    public void createProduct(CreateProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        Product createdProduct = productDomainService.createProduct(
                request.getName(),
                request.getDescription(),
                BigDecimal.valueOf(request.getPrice()),
                request.getSkuCode()
        );
        responseObserver.onNext(mapToProductResponse(createdProduct));
        responseObserver.onCompleted();
    }

    // 4. CẬP NHẬT SẢN PHẨM (GỌI DOMAIN SERVICE ĐỂ CHẠY LOGIC DDD)
    @Override
    public void updateProduct(UpdateProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        Product updatedProduct = productDomainService.updateProduct(
                request.getSkuCode(),
                request.getName(),
                request.getDescription(),
                BigDecimal.valueOf(request.getPrice())
        );
        responseObserver.onNext(mapToProductResponse(updatedProduct));
        responseObserver.onCompleted();
    }

    // 5. XÓA SẢN PHẨM (THEO ID NGHIỆP VỤ: SP001, SP002...)
    @Override
    public void deleteProduct(DeleteProductRequest request, StreamObserver<DeleteProductResponse> responseObserver) {
        productRepository.deleteById(request.getId());
        DeleteProductResponse response = DeleteProductResponse.newBuilder()
                .setMessage("Đã xóa thành công sản phẩm có ID: " + request.getId())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // HÀM PHỤ TRỢ: CHUYỂN ĐỔI ENTITY SANG GRPC RESPONSE
    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.newBuilder()
                .setId(product.getId()) // Trả về SP001, SP002...
                .setName(product.getName())
                .setDescription(product.getDescription() != null ? product.getDescription() : "")
                .setPrice(product.getPrice().doubleValue())
                .setSkuCode(product.getSkuCode())
                .setSkuCode(product.getSkuCode() != null ? product.getSkuCode() : "")
                .build();
    }
}