package com.task.solva.repository;

import com.task.solva.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    @Query(value = "SELECT er FROM ExchangeRate er WHERE 1 = 1 AND er.name = UPPER(:currency) ORDER BY er.date DESC LIMIT 1")
    ExchangeRate getCurrentExchangeRate(String currency);
}
