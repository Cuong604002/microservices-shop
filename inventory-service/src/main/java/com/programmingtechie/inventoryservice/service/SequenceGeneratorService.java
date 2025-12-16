package com.programmingtechie.inventoryservice.service;

import com.programmingtechie.inventoryservice.model.DatabaseSequence;
import com.programmingtechie.inventoryservice.repository.DatabaseSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SequenceGeneratorService {
    private final DatabaseSequenceRepository repository;

    public long generateSequence(String seqName) {
        DatabaseSequence counter = repository.findById(seqName)
                .orElse(new DatabaseSequence(seqName, 0L));

        long nextValue = counter.getSeq() + 1;
        counter.setSeq(nextValue);

        repository.save(counter);
        return nextValue;
    }
}