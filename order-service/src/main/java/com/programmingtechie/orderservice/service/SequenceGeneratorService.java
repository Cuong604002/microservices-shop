package com.programmingtechie.orderservice.service;

import com.programmingtechie.orderservice.model.DatabaseSequence;
import com.programmingtechie.orderservice.repository.DatabaseSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SequenceGeneratorService {

    private final DatabaseSequenceRepository repository;

    @Transactional
    public long generateSequence(String seqName) {
        // Tìm bộ đếm theo tên, nếu chưa có thì tạo mới bắt đầu từ 0
        DatabaseSequence counter = repository.findById(seqName)
                .orElse(new DatabaseSequence(seqName, 0L));

        // Tăng giá trị lên 1
        long nextValue = counter.getSeq() + 1;
        counter.setSeq(nextValue);

        // Lưu lại vào Database
        repository.save(counter);

        return nextValue;
    }
}