package com.programmingtechie.orderservice.repository;

import com.programmingtechie.orderservice.model.DatabaseSequence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseSequenceRepository extends JpaRepository<DatabaseSequence, String> {
}