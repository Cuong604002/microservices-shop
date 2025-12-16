package com.programmingtechie.inventoryservice.repository;

import com.programmingtechie.inventoryservice.model.DatabaseSequence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseSequenceRepository extends JpaRepository<DatabaseSequence, String> {
}