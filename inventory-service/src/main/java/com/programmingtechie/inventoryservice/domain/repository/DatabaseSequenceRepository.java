package com.programmingtechie.inventoryservice.domain.repository;

import com.programmingtechie.inventoryservice.domain.model.DatabaseSequence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseSequenceRepository extends JpaRepository<DatabaseSequence, String> {
}