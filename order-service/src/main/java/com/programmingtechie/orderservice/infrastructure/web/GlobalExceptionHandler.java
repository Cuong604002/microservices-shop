package com.programmingtechie.orderservice.infrastructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice // Đánh dấu đây là nơi chuyên bắt lỗi cho toàn bộ ứng dụng
public class GlobalExceptionHandler {

    // 1. Bắt lỗi logic nghiệp vụ (Ví dụ: Hết hàng, Không tìm thấy đơn...)
    // Trong Service mình dùng IllegalArgumentException và RuntimeException
    @ExceptionHandler({IllegalArgumentException.class, RuntimeException.class})
    public ResponseEntity<Object> handleBusinessExceptions(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Lỗi xử lý đơn hàng");
        body.put("message", ex.getMessage()); // Lấy câu thông báo bạn viết trong Service

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // 2. Bắt tất cả các lỗi còn lại (Lỗi hệ thống không lường trước)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Lỗi hệ thống");
        body.put("message", "Đã xảy ra lỗi không mong muốn: " + ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}