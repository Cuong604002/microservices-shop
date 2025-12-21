package com.programmingtechie.orderservice.infrastructure.kafka;

import com.programmingtechie.orderservice.domain.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderPlacedEventListener {

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        // Logic giả lập gửi Email/SMS khi nhận được tin nhắn từ Kafka
        log.info("Đã nhận được sự kiện đặt hàng cho Order Number: {}", orderPlacedEvent.getOrderNumber());
        // Sau này bạn có thể viết code gửi email thật ở đây
    }
}