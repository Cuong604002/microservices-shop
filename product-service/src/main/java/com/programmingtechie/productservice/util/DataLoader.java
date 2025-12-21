//pacôage com.programmingtechie.productservice.util;
//
//import com.programmingtechie.productservice.domain.model.Product;
//import com.programmingtechie.productservice.domain.repository.ProductRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//
//@Component
//@RequiredArgsConstructor
//public class DataLoader implements CommandLineRunner {
//
//    private final ProductRepository productRepository;
//    // Thêm sequence repository nếu bạn có
//
//    @Override
//    public void run(String... args) throws Exception {
//        // DÒNG NÀY SẼ XÓA SẠCH DỮ LIỆU CŨ KHI BẠN START APP
//        productRepository.deleteAll();
//
//        System.out.println("--- Đã xóa sạch dữ liệu cũ trong MongoDB ---");
//
//        // Bạn có thể để trống hoặc thêm code tạo dữ liệu mẫu ở đây
//    }
//}