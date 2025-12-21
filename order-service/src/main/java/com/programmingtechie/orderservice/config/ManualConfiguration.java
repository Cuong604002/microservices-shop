package com.programmingtechie.orderservice.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration(proxyBeanMethods = false)
public class ManualConfiguration {
    // Hiện tại chúng ta để cấu hình Kafka đơn giản.
    // Nếu bạn cần cấu hình Topic "notificationTopic" tự động, có thể thêm NewTopic Bean ở đây.
    // Tạm thời để trống hoặc giữ nguyên code cũ của bạn nếu có.
}