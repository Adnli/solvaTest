package com.task.solva.repository;

import com.task.solva.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    @Query(value = "SELECT * FROM Operation o WHERE 1 = 1 " +
            "AND o.user_id = :id " +
            "AND o.limit_exceeded = true " +
            "AND o.date BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    List<Operation> getAllLimits(Long id, Date startDate, Date endDate);
}
